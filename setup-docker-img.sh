#!/bin/bash

docker build -t eb-api .
docker tag eb-api 275838/eb-api
docker push 275838/eb-api
docker rmi eb-api 275838/eb-api
