package com.tienda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExamenDiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExamenDiApplication.class, args);
        System.out.println("🚀 Tienda Online corriendo en http://localhost:8080 🚀");
    }
}
