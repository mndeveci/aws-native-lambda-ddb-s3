- First, create Docker image to use native image generation (which contains graalvm-jdk and native-image)
  
   `docker build -t mndeveci/graal-native-image-graal:latest .`
  
- Compile and create fat jar for lambda
  
   `./gradlew clean shadowJar`
  
- Then, run the following command to generate native app, which could be run at any linux 64bit system

    `docker run -it \
     -v /Users/mndeveci/workspace/graal/aws-ddb-s3/build/libs/:/project --rm \
     mndeveci/graal-native-image-graal:latest \
     --static -cp . -jar aws-native-lambda-ddb-s3-1.0-SNAPSHOT.jar \
     --no-server \
     --no-fallback \
     --verbose \
     --enable-all-security-services
     -H:Name=app \
     -H:+ReportExceptionStackTraces  \
     -H:-AllowVMInspection \
     -H:-UseServiceLoaderFeature \
     -R:-InstallSegfaultHandler \
     -H:EnableURLProtocols=http,https \
     -H:IncludeResources="logging.properties" \
     -H:Class=com.mndeveci.NativeExecutor \
     -H:ReflectionConfigurationFiles=config.json \
     -H:DynamicProxyConfigurationFiles=proxy-config.json \
     -H:DynamicProxyConfigurationResources=proxy-config.json \
     -H:ResourceConfigurationFiles=resource-config.json \`
     
 ### References
 - https://royvanrijn.com/blog/2018/09/part-1-java-to-native-using-graalvm/
   - https://github.com/royvanrijn/graalvm-native-microservice
 - https://medium.com/@mathiasdpunkt/fighting-cold-startup-issues-for-your-kotlin-lambda-with-graalvm-39d19b297730
 - https://qiita.com/kencharos/items/69e43965515f368bc4a3
     - https://github.com/kencharos/try-graal-lambda
 - https://engineering.opsgenie.com/run-native-java-using-graalvm-in-aws-lambda-with-golang-ba86e27930bf
 
 