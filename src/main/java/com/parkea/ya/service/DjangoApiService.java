package com.parkea.ya.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.parkea.ya.dto.DjangoApiResponse;
import com.parkea.ya.dto.ParkingRegistrationRequest;

@Service
public class DjangoApiService {
    
    @Value("${django.api.base-url:http://localhost:8000}")
    private String djangoBaseUrl;
    
    @Value("${django.api.token:}")
    private String apiToken;
    
    private final RestTemplate restTemplate;
    
    public DjangoApiService() {
        this.restTemplate = new RestTemplate();
    }
    
    public DjangoApiResponse enviarSolicitudAcceso(ParkingRegistrationRequest solicitud) {
        try {
            String url = djangoBaseUrl + "/api/parking/approval-requests/";
            
            // Preparar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (apiToken != null && !apiToken.isEmpty()) {
                headers.set("Authorization", "Token " + apiToken);
            }
            
            // Preparar body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("nombre_solicitante", solicitud.getNombre());
            requestBody.put("email", solicitud.getEmail());
            requestBody.put("telefono", solicitud.getTelefono());
            requestBody.put("empresa", solicitud.getEmpresa());
            requestBody.put("mensaje", solicitud.getMensaje());
            requestBody.put("tipo_solicitud", "ACCESO_PANEL");
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Enviar solicitud
            ResponseEntity<Map> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, Map.class);
            
            return procesarRespuesta(response);
            
        } catch (HttpClientErrorException e) {
            return new DjangoApiResponse(false, "Error del cliente: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            return new DjangoApiResponse(false, "Error del servidor Django: " + e.getStatusCode());
        } catch (Exception e) {
            return new DjangoApiResponse(false, "Error de conexión: " + e.getMessage());
        }
    }
    
    public DjangoApiResponse obtenerEstadisticasPanel() {
        try {
            String url = djangoBaseUrl + "/api/dashboard/stats/";
            
            HttpHeaders headers = new HttpHeaders();
            if (apiToken != null && !apiToken.isEmpty()) {
                headers.set("Authorization", "Token " + apiToken);
            }
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            return procesarRespuesta(response);
            
        } catch (Exception e) {
            return new DjangoApiResponse(false, "Error al obtener estadísticas: " + e.getMessage());
        }
    }
    
    public DjangoApiResponse obtenerSolicitudesPendientes() {
        try {
            String url = djangoBaseUrl + "/api/parking/approval-requests/pendientes/";
            
            HttpHeaders headers = new HttpHeaders();
            if (apiToken != null && !apiToken.isEmpty()) {
                headers.set("Authorization", "Token " + apiToken);
            }
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            return procesarRespuesta(response);
            
        } catch (Exception e) {
            return new DjangoApiResponse(false, "Error al obtener solicitudes: " + e.getMessage());
        }
    }
    
    private DjangoApiResponse procesarRespuesta(ResponseEntity<Map> response) {
        DjangoApiResponse apiResponse = new DjangoApiResponse();
        apiResponse.setStatusCode(response.getStatusCodeValue());
        
        if (response.getStatusCode().is2xxSuccessful()) {
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Solicitud procesada exitosamente");
            apiResponse.setData(response.getBody());
        } else {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Error en la respuesta del servidor");
            apiResponse.setData(response.getBody());
        }
        
        return apiResponse;
    }
}