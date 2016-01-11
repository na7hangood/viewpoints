#!/usr/bin/env bash

cd public

npm run build-dev &

cd ..

AWS_PROFILE=composer sbt run
