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

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

object WorkerQueueApp extends App {


  val configuration = getConfiguration()
  val workerTypeVal = configuration.getString("worker.type")
  val workerType = WorkerType.withName(workerTypeVal)

  implicit val actorSystem = ActorSystem()
  implicit val actorMaterializer = ActorMaterializer()

  QueueWorker(workerType,configuration)

  def getConfiguration(): Config = {

    val workerFile = new File("/etc/worker/worker.conf");

    if(workerFile.exists) {
      ConfigFactory.parseFile(workerFile)
    } else {
      ConfigFactory.parseResources("worker.conf").resolve()
    }
  }

}
