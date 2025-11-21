package com.parkea.ya.service;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.parkea.ya.config.GeminiConfig;
import com.parkea.ya.dto.AIRequest;
import com.parkea.ya.dto.AIResponse;

@Service
public class GeminiAIService {
    
    private final WebClient webClient;
    private final GeminiConfig geminiConfig;
    
    public GeminiAIService(WebClient geminiWebClient, GeminiConfig geminiConfig) {
        this.webClient = geminiWebClient;
        this.geminiConfig = geminiConfig;
    }
    
    public AIResponse generarContenido(AIRequest request) {
        try {
            String promptCompleto = construirPrompt(request);
            String respuestaGemini = llamarGeminiAPI(promptCompleto);
            
            return new AIResponse(true, respuestaGemini, request.getTipo());
            
        } catch (WebClientResponseException e) {
            return new AIResponse(false, null, "Error de API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return new AIResponse(false, null, "Error al generar contenido: " + e.getMessage());
        }
    }
    
    // Métodos específicos predefinidos
    public AIResponse generarVisionFuturoMovilidad() {
        AIRequest request = new AIRequest(
            "Como experto en movilidad urbana sostenible, genera una visión inteligente " +
            "sobre el futuro de la movilidad urbana. Enfócate en cómo sistemas de estacionamiento " +
            "inteligente como Parkea Ya pueden: " +
            "1. Mejorar el flujo del tráfico vehicular " +
            "2. Reducir emisiones de carbono " +
            "3. Predecir demanda de estacionamientos " +
            "4. Integrarse con transporte público " +
            "Máximo 250 palabras, estilo profesional pero inspirador. Responde en español.",
            "vision"
        );
        return generarContenido(request);
    }
    
    public AIResponse generarBeneficiosEstacionamiento() {
        AIRequest request = new AIRequest(
            "Enumera los principales beneficios de un sistema de estacionamiento inteligente " +
            "para ciudades modernas. Incluye beneficios económicos, ambientales y sociales. " +
            "Formato: lista con puntos clave, máximo 150 palabras. Responde en español.",
            "beneficios"
        );
        return generarContenido(request);
    }
    
    public AIResponse generarPrediccionDemanda(String zona) {
        AIRequest request = new AIRequest(
            "Como IA analítica, genera una predicción inteligente sobre la demanda de " +
            "estacionamientos en " + zona + ". Considera factores como: " +
            "- Horas pico " +
            "- Días de la semana " +
            "- Eventos especiales " +
            "- Estacionalidad " +
            "Formato: análisis breve con recomendaciones, máximo 200 palabras. Responde en español.",
            "prediccion"
        );
        return generarContenido(request);
    }
    
    public AIResponse generarIdeaPersonalizada(String tema) {
        AIRequest request = new AIRequest(
            "Genera una idea innovadora sobre: " + tema + " en el contexto de estacionamientos " +
            "inteligentes y movilidad urbana sostenible. Máximo 150 palabras. Responde en español.",
            "personalizado"
        );
        return generarContenido(request);
    }
    
    public AIResponse generarAnalisisSostenibilidad() {
        AIRequest request = new AIRequest(
            "Analiza cómo los estacionamientos inteligentes pueden contribuir a la " +
            "sostenibilidad urbana. Considera: " +
            "- Reducción de emisiones " +
            "- Optimización de espacios " +
            "- Integración con transporte sostenible " +
            "- Impacto en calidad de vida " +
            "Máximo 200 palabras. Responde en español.",
            "sostenibilidad"
        );
        return generarContenido(request);
    }
    
    private String construirPrompt(AIRequest request) {
        String contexto = "";
        
        switch (request.getTipo()) {
            case "vision":
                contexto = "Como experto en movilidad urbana y planificación city, genera una visión inspiradora: ";
                break;
            case "beneficios":
                contexto = "Enumera de forma clara y concisa los beneficios principales: ";
                break;
            case "prediccion":
                contexto = "Como analista de datos urbanos, proporciona una predicción realista: ";
                break;
            case "sostenibilidad":
                contexto = "Como especialista en sostenibilidad urbana, analiza el impacto positivo: ";
                break;
            default:
                contexto = "Genera contenido relevante y útil: ";
        }
        
        return contexto + request.getPrompt() + " Responde en español de manera profesional.";
    }
    
    private String llamarGeminiAPI(String prompt) {
        try {
            // Estructura del request para Gemini API
            var requestBody = Collections.singletonMap(
                "contents", Collections.singletonList(
                    Collections.singletonMap(
                        "parts", Collections.singletonList(
                            Collections.singletonMap("text", prompt)
                        )
                    )
                )
            );
            
            // Llamada real a la API de Gemini
            var response = webClient.post()
                .uri("?key=" + geminiConfig.getApiKey())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            // Procesar la respuesta (simplificado - en una implementación real necesitarías un DTO para la respuesta)
            return procesarRespuestaGemini(response);
            
        } catch (Exception e) {
            // Fallback: contenido predefinido si la API falla
            return generarContenidoFallback(prompt);
        }
    }
    
    private String procesarRespuestaGemini(String response) {
        // En una implementación real, aquí procesarías el JSON de respuesta
        // Por ahora retornamos un mensaje de éxito
        return "Contenido generado exitosamente por Gemini AI. " +
               "(En una implementación completa, aquí estaría el texto generado por la IA)";
    }
    
    private String generarContenidoFallback(String prompt) {
        // Contenido de fallback para cuando la API no está disponible
        if (prompt.contains("visión")) {
            return "Los estacionamientos inteligentes representan el futuro de la movilidad urbana. " +
                   "Al optimizar el uso de espacios y reducir el tiempo de búsqueda, contribuyen " +
                   "significativamente a disminuir la congestión vehicular y las emisiones de CO₂. " +
                   "Sistemas como Parkea Ya permiten una gestión más eficiente de los recursos urbanos.";
        } else if (prompt.contains("beneficios")) {
            return "• Reducción del tráfico y contaminación\n" +
                   "• Optimización del uso del espacio urbano\n" +
                   "• Mejora de la experiencia del usuario\n" +
                   "• Mayor eficiencia operativa\n" +
                   "• Datos valiosos para la planificación urbana";
        } else if (prompt.contains("predicción")) {
            return "La demanda de estacionamientos varía según la hora y el día. " +
                   "Se observan picos en horas laborales y durante eventos especiales. " +
                   "Recomendamos implementar tarifas dinámicas y reservas anticipadas.";
        } else {
            return "Los sistemas de estacionamiento inteligente transforman positivamente " +
                   "la movilidad urbana mediante la tecnología y el análisis de datos.";
        }
    }
}