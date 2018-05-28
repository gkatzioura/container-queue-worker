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

import java.util.UUID

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sqs.model.SendMessageRequest
import com.amazonaws.services.sqs.{AmazonSQSClientBuilder}
import org.scalatest.FunSuite

class SendMessageTest extends FunSuite{

  val endpoint = "http://localhost:9324";
  val region = "elasticmq";
  val accessKey = "x";
  val secretKey = "x";

  test("Send a message") {

    val client = AmazonSQSClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
      .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
      .build();

    client.createQueue("test-queue")
    val sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setQueueUrl("http://localhost:9324/queue/test-queue")
    sendMessageRequest.setMessageBody(UUID.randomUUID().toString)
    client.sendMessage(sendMessageRequest)
  }

}
