package com.nubank.challenge.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubank.challenge.domain.model.dto.OperationRequest;
import com.nubank.challenge.domain.service.OperationService;
import com.nubank.challenge.domain.service.OperationServiceFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private OperationServiceFactory serviceFactory;
    private ObjectMapper objectMapper;

    public Application(OperationServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.objectMapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines().forEach(this::process);
    }

    public void process(String input) {
        try {
            final OperationRequest operationRequest = objectMapper.readValue(input,
                    OperationRequest.class);
            final OperationService service = serviceFactory.get(operationRequest);
            System.out.println(objectMapper.writeValueAsString(service.execute(operationRequest)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
