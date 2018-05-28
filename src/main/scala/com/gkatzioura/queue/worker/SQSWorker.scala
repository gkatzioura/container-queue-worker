/*
 * Copyright 2018 Emmanouil Gkatziouras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gkatzioura.queue.worker

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream._
import akka.stream.alpakka.sqs.MessageAction
import akka.stream.alpakka.sqs.scaladsl.{SqsAckFlow, SqsAckSink, SqsSource}
import akka.stream.scaladsl.{Flow, GraphDSL, Partition, RestartSource, RunnableGraph, Sink}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.sqs.model.Message
import com.amazonaws.services.sqs.{AmazonSQSAsync, AmazonSQSAsyncClientBuilder}
import com.google.gson.Gson
import com.typesafe.config.Config

import scala.concurrent.Future
import scala.concurrent.duration._

class SQSWorker(configuration: Config) (implicit actorSystem: ActorSystem,actorMaterializer: ActorMaterializer) extends QueueWorker {

  implicit val executionContext = actorSystem.dispatcher

  val endpoint = configuration.getString("worker.aws.queue-endpoint")
  val region = configuration.getString("worker.aws.region")
  val serverEndpoint = configuration.getString("worker.server-endpoint")

  implicit val sqs : AmazonSQSAsync = createSQS()

  val source =
    RestartSource.withBackoff(minBackoff = 1 second,maxBackoff = 30 seconds,randomFactor = 0.2) { () =>
    SqsSource(endpoint)
      .log("before-map")
      .withAttributes(Attributes.logLevels(onElement = Logging.DebugLevel))
      .mapAsync(parallelism = 2) (
        request => {
          Http().singleRequest((messageToRequest(serverEndpoint, request)))
            .map(response => {requestToMessage(request,response)})
            .recoverWith {
              case e => {
                Logging.getLogger(actorSystem,this).error("Could not process message {} ignoring due to error {}",request.getMessageId,e);
                Future.successful((request,false))
              }
          }
        }
      )
    }

  val g = GraphDSL.create() { implicit b =>

    import GraphDSL.Implicits._
    val partition = b.add(Partition[(Message,Boolean)](2, mex => if (mex._2) 0 else 1))
    source ~> partition.in
    partition.out(0) ~> Flow[(Message,Boolean)].map { me => (me._1, MessageAction.Delete)} ~> SqsAckSink(endpoint)
    partition.out(1) ~> Flow[(Message,Boolean)].map { me => (me._1, MessageAction.Ignore)} ~> SqsAckFlow(endpoint) ~> Sink.ignore

    ClosedShape
  }

  RunnableGraph.fromGraph(g).run

  def createSQS() : AmazonSQSAsync = {

    AmazonSQSAsyncClientBuilder
      .standard()
      .withEndpointConfiguration(new EndpointConfiguration(endpoint, region))
      .build()
  }

  def messageToRequest(serverEndpoint: String, message: Message) : HttpRequest = {

    val httpEntity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, new Gson().toJson(message) )
    HttpRequest(HttpMethods.POST,serverEndpoint,List(),httpEntity)
  }

  def requestToMessage(message: Message,httpResponse: HttpResponse): (Message,Boolean) = {

    val logger = Logging.getLogger(actorSystem,this)

    if(httpResponse.status==StatusCodes.OK) {
      logger.info("Successfully processed message {}",message.getMessageId)
      (message,true)
    } else {
      logger.error("Could not process message {} status {}",message.getMessageId,httpResponse.status)
      (message,false)
    }
  }

}
