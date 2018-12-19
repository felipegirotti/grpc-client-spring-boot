# GRPC Client
This is a simple example to use the gRPC into the Spring-Boot app   
For more details about the server take a look here: [https://github.com/felipegirotti/grpc-server](https://github.com/felipegirotti/grpc-server)

- Dependencies of pom
```xml
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-netty-shaded</artifactId>
    <version>1.17.1</version>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-protobuf</artifactId>
    <version>1.17.1</version>
</dependency>
<dependency>
    <groupId>io.grpc</groupId>
    <artifactId>grpc-stub</artifactId>
    <version>1.17.1</version>
</dependency>
```

- Add the build dependencies
```xml
<build>
    <extensions>
        <extension>
            <groupId>kr.motd.maven</groupId>
            <artifactId>os-maven-plugin</artifactId>
            <version>1.5.0.Final</version>
        </extension>
    </extensions>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
            <version>0.5.1</version>
            <configuration>
                <protocArtifact>com.google.protobuf:protoc:3.5.1-1:exe:${os.detected.classifier}</protocArtifact>
                <pluginId>grpc-java</pluginId>
                <pluginArtifact>io.grpc:protoc-gen-grpc-java:1.17.1:exe:${os.detected.classifier}</pluginArtifact>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>compile-custom</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- Proto file must be into `/src/main/proto` (should the same file of the server)
```
syntax = "proto3";

package com.example;

option java_multiple_files = true;


message Hello {
    string firstname = 1;
    string lastname = 2;
}

message Greeting {
    string greeting = 1;
}

service GreetingService {
    rpc hello(Hello) returns (Greeting);
}
```

- Run `mvn clean package` to create the files of client gRPC base and the request and response  

- The files are created into the folder *target/generated-sources/protobuf* (Make sure your IDE read those files as source)

- Create a channel
```java
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8092)
                .usePlaintext()
                .build();
```

- Create a request to server
```java
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        Greeting hello = stub.hello(Hello.newBuilder().setFirstname("Felipe").setLastname("Girotti").build());

        return hello.getGreeting();
```
 

:tada: Done! 

Just running your application `mvn spring-boot:run` or build and run the jar.

For tests purpose we create a simple controller with the api route `GET /hello/{firstName}/{lastName}`

`curl http://localhost:9199/hello/felipe/girotti`

You see `Hello felipe girotti`