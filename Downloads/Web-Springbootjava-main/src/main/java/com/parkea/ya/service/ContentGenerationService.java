package com.parkea.ya.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parkea.ya.dto.AIRequest;
import com.parkea.ya.dto.AIResponse;

@Service
public class ContentGenerationService {
    
    @Autowired
    private GeminiAIService geminiAIService;
    
    public Map<String, String> generarContenidoHomepage() {
        Map<String, String> contenido = new HashMap<>();
        
        try {
            // Generar título principal
            AIRequest tituloRequest = new AIRequest(
                "Genera un título atractivo y moderno para una plataforma de estacionamientos inteligentes " +
                "llamada 'Parkea Ya'. Debe ser corto, memorable y transmitir innovación. Máximo 6 palabras.",
                "titulo"
            );
            AIResponse tituloResponse = geminiAIService.generarContenido(tituloRequest);
            
            // Generar descripción
            AIRequest descripcionRequest = new AIRequest(
                "Escribe una descripción persuasiva para Parkea Ya, una plataforma de estacionamiento inteligente. " +
                "Destaca beneficios como: reducción de tiempo de búsqueda, menor contaminación, " +
                "y optimización de espacios urbanos. Máximo 150 caracteres.",
                "descripcion"
            );
            AIResponse descripcionResponse = geminiAIService.generarContenido(descripcionRequest);
            
            // Generar llamada a la acción
            AIRequest ctaRequest = new AIRequest(
                "Crea una llamada a la acción (Call to Action) persuasiva para que usuarios " +
                "se registren en Parkea Ya. Debe ser motivadora y crear urgencia. Máximo 8 palabras.",
                "cta"
            );
            AIResponse ctaResponse = geminiAIService.generarContenido(ctaRequest);
            
            contenido.put("titulo", tituloResponse.isSuccess() ? tituloResponse.getContenido() : "Parkea Ya - Estacionamiento Inteligente");
            contenido.put("descripcion", descripcionResponse.isSuccess() ? descripcionResponse.getContenido() : "Encuentra estacionamiento rápido y reduce tu huella de carbono");
            contenido.put("cta", ctaResponse.isSuccess() ? ctaResponse.getContenido() : "Únete a la movilidad del futuro");
            contenido.put("estado", "generado");
            
        } catch (Exception e) {
            contenido.put("error", "Error generando contenido: " + e.getMessage());
            contenido.put("estado", "error");
        }
        
        return contenido;
    }
    
    public String generarTestimonioIA() {
        AIRequest request = new AIRequest(
            "Genera un testimonio ficticio pero realista de un usuario satisfecho con " +
            "un sistema de estacionamiento inteligente. Incluye beneficios específicos " +
            "como ahorro de tiempo, reducción de estrés y contribución al medio ambiente. " +
            "Máximo 100 palabras. Formato: cita entre comillas con nombre y ciudad ficticios.",
            "testimonio"
        );
        
        AIResponse response = geminiAIService.generarContenido(request);
        return response.isSuccess() ? response.getContenido() : 
            "\"Parkea Ya me ahorra 15 minutos diarios de búsqueda. ¡Increíble!\" - Carlos, Lima";
    }
    
    public Map<String, String> generarFAQs() {
        Map<String, String> faqs = new HashMap<>();
        
        try {
            AIRequest request = new AIRequest(
                "Genera 3 preguntas frecuentes (FAQs) sobre estacionamientos inteligentes " +
                "con sus respuestas. Incluye temas como: cómo funciona, beneficios ambientales, " +
                "y seguridad. Formato: Pregunta: [pregunta] Respuesta: [respuesta breve]. " +
                "Máximo 50 palabras por respuesta.",
                "faqs"
            );
            
            AIResponse response = geminiAIService.generarContenido(request);
            
            if (response.isSuccess()) {
                // Procesar la respuesta para separar preguntas y respuestas
                String contenido = response.getContenido();
                String[] lineas = contenido.split("\n");
                
                for (int i = 0; i < Math.min(lineas.length, 3); i++) {
                    faqs.put("pregunta" + (i + 1), lineas[i]);
                }
            } else {
                // FAQs por defecto
                faqs.put("pregunta1", "¿Cómo funciona Parkea Ya?");
                faqs.put("respuesta1", "Usamos IA para predecir disponibilidad y optimizar rutas.");
                faqs.put("pregunta2", "¿Qué beneficios ambientales ofrece?");
                faqs.put("respuesta2", "Reduce emisiones al minimizar el tiempo de búsqueda de estacionamiento.");
                faqs.put("pregunta3", "¿Es seguro el sistema?");
                faqs.put("respuesta3", "Sí, contamos con múltiples capas de seguridad y verificación.");
            }
            
        } catch (Exception e) {
            faqs.put("error", "Error generando FAQs: " + e.getMessage());
        }
        
        return faqs;
    }
    
    public String generarVisionFuturo() {
        AIRequest request = new AIRequest(
            "Describe una visión inspiradora del futuro de la movilidad urbana con " +
            "estacionamientos inteligentes. Incluye conceptos como: ciudades más verdes, " +
            "tráfico optimizado, integración con transporte público, y calidad de vida mejorada. " +
            "Máximo 200 palabras. Estilo: motivador y positivo.",
            "vision"
        );
        
        AIResponse response = geminiAIService.generarContenido(request);
        return response.isSuccess() ? response.getContenido() : 
            "Imaginamos ciudades donde encontrar estacionamiento sea instantáneo, " +
            "el tráfico fluya suavemente y las emisiones se reduzcan significativamente. " +
            "Un futuro donde la tecnología sirva para crear espacios urbanos más habitables y sostenibles.";
    }
}