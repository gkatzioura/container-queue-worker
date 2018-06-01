# container-queue-worker

The container-queue-worker projects aims to provide an easy way to migrate your elastic beanstalk workers to a docker orchestration system.

The projects works by creating a configuration file and mounting it to your container on the '/etc/worker/worker.conf' path.

```
worker {
  type =  sqs
  server-endpoint = http://{docker-service}
  aws {
    queue-endpoint =  http://{amazon queue endpoint}
  }
}
```

Another way is to configure the queue worker using environmental variables

```
WORKER_TYPE=sqs
WORKER_SERVER_ENDPOINT=http://{docker-service}
WORKER_AWS_QUEUE_ENDPOINT=http://{amazon queue endpoint}
```

Keep in mind that you should provide the aws credentials configuration through environment variables
This has to be done regardless of the configuration approach that you have used. 

```
AWS_DEFAULT_REGION: eu-west-1
AWS_ACCESS_KEY_ID: access-key
AWS_SECRET_ACCESS_KEY: secret-key
```

The image is available on [dockerhub](https://hub.docker.com/r/gkatzioura/container-queue-worker/) 

Licensed under Apache, Version 2.0