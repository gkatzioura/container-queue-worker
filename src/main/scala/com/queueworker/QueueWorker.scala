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

package com.queueworker

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import WorkerType.WorkerType
import com.typesafe.config.Config

trait QueueWorker {
}

object QueueWorker {

  def apply(workerType: WorkerType,config: Config) (implicit actorSystem: ActorSystem, actorMaterializer: ActorMaterializer): QueueWorker = {

    if(workerType==WorkerType.AMAZON_SQS) new SQSWorker(config)
    else throw new IllegalArgumentException("Provide a valid worker");
  }
}
