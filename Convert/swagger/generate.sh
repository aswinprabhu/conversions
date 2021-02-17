#! /bin/sh

# clean
rm -rf ../wavefront-java-sdk

# create dir
mkdir ../wavefront-java-sdk

# generate sdk
swagger-codegen generate -i https://metrics.wavefront.com/api/v2/swagger.json -c swagger-config.json -l java -o ../wavefront-java-sdk
