package com.example.grpc.client;

import com.example.Greeting;
import com.example.GreetingServiceGrpc;
import com.example.Hello;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
public class GreetingController {

    ManagedChannel channel;

    @PostConstruct
    public void channel() {
        channel = ManagedChannelBuilder.forAddress("localhost", 8092)
                .usePlaintext()
                .build();
    }

    @GetMapping("/hello/{name}/{lastName}")
    public String hello(@PathVariable String name, @PathVariable String lastName) {
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);

        Greeting hello = stub.hello(Hello.newBuilder().setFirstname(name).setLastname(lastName).build());

        return hello.getGreeting();
    }
}
