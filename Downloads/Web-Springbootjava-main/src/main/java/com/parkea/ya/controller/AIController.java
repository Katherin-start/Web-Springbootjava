package com.parkea.ya.controller;

import com.parkea.ya.dto.AIRequest;
import com.parkea.ya.dto.AIResponse;  // ← CORREGIDO: AIResponse no AlResponse
import com.parkea.ya.service.GeminiAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    
    @Autowired
    private GeminiAIService geminiAIService;
    
    @PostMapping("/generar")
    public AIResponse generarContenido(@RequestBody AIRequest request) {  // ← CORREGIDO
        return geminiAIService.generarContenido(request);
    }
    
    @GetMapping("/vision-futuro")
    public AIResponse getVisionFuturoMovilidad() {  // ← CORREGIDO
        return geminiAIService.generarVisionFuturoMovilidad();
    }
    
    @GetMapping("/beneficios")
    public AIResponse getBeneficiosEstacionamiento() {  // ← CORREGIDO
        return geminiAIService.generarBeneficiosEstacionamiento();
    }
    
    @GetMapping("/prediccion")
    public AIResponse getPrediccionDemanda(@RequestParam(defaultValue = "centro urbano") String zona) {  // ← CORREGIDO
        return geminiAIService.generarPrediccionDemanda(zona);
    }
    
    @GetMapping("/idea")
    public AIResponse generarIdea(@RequestParam String tema) {  // ← CORREGIDO
        return geminiAIService.generarIdeaPersonalizada(tema);
    }
    
    @GetMapping("/test")
    public AIResponse testAPI() {  // ← CORREGIDO
        AIRequest request = new AIRequest(
            "Responde brevemente en español: ¿Está funcionando la conexión con Gemini?",
            "test"
        );
        return geminiAIService.generarContenido(request);
    }
}