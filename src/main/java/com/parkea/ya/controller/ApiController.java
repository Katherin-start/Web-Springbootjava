package com.parkea.ya.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/status")
    public Map<String, String> status() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "active");
        status.put("service", "Parkea Ya API");
        status.put("version", "1.0.0");
        return status;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("nombre", "Parkea Ya");
        info.put("descripcion", "Sistema de estacionamiento inteligente");
        info.put("tecnologias", new String[]{"Spring Boot", "PostgreSQL", "Gemini AI", "Thymeleaf"});
        return info;
    }
}