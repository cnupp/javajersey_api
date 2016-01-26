DOCKER_HOST=192.168.99.100
APP_NAME=java-jersey-test-app2
TMP_IMAGE=hub.deepi.cn/test1111
STACK_BUILD_IMAGE=hub.deepi.cn/javajersey-test-build
docker run \
    --privileged \
    --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v `pwd`:/codebase \
    -e HOST=$DOCKER_HOST \
    -e APP_NAME=$APP_NAME \
    -e IMAGE=$TMP_IMAGE \
    $STACK_BUILD_IMAGE
