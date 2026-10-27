package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication   // «это Spring Boot-приложение»: включает автонастройку
@RestController          // класс отвечает на HTTP-запросы
public class DemoApplication {

    // Берём версию из переменной окружения APP_VERSION, если её нет — "dev"
    @Value("${APP_VERSION:dev}")
    private String version;

    @GetMapping("/")     // ответ на GET http://адрес:8080/
    public String hello() {
        return "Hello from demo-service! version=" + version + "\n";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);   // запуск приложения
    }
}
