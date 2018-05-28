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

object WorkerType extends Enumeration {
  type WorkerType = Value
  val AMAZON_SQS = Value("sqs")
  val AZURE_STORAGE_QUEUE = Value("azure_storage_queue")
  val GOOGLE_PUB_SUB = Value("google_pub_sub")
}
