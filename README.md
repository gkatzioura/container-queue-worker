# container-queue-worker

The container-queue-worker projects aims to provide an easy way to migrate your elastic beanstalk workers to a docker orchestration system.

The projects works by creating a configuration file and mounting it to your container on the '/etc/worker/worker.conf' path.

```
worker {
  type =  sqs
  server-endpoint = http://{docker-service}
  aws {
    queue-endpoint =  http://{amazon queue endpoint}
    region = {aws region}
    accessKey = {aws access key}
    secretKey = {aws secret key}
  }
}
```

Another way is to configure the queue worker using environmental variables

```
WORKER_TYPE=sqs
WORKER_SERVER_ENDPOINT=http://{docker-service}
WORKER_AWS_QUEUE_ENDPOINT=http://{amazon queue endpoint}
WORKER_AWS_REGION={aws region}
WORKER_AWS_ACCESS_KEY={aws access key}
WORKER_AWS_SECRET_KEY={aws secret key}
```


Licensed under Apache, Version 2.0