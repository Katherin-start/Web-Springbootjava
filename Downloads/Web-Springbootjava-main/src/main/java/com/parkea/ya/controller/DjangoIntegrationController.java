package com.parkea.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkea.ya.dto.DjangoApiResponse;
import com.parkea.ya.dto.ParkingRegistrationRequest;
import com.parkea.ya.service.DjangoApiService;

@RestController
@RequestMapping("/api/django")
public class DjangoIntegrationController {
    
    @Autowired
    private DjangoApiService djangoApiService;
    
    @PostMapping("/solicitar-acceso-panel")
    public DjangoApiResponse solicitarAccesoPanel(@RequestBody ParkingRegistrationRequest solicitud) {
        return djangoApiService.enviarSolicitudAcceso(solicitud);
    }
    
    @GetMapping("/estadisticas")
    public DjangoApiResponse obtenerEstadisticas() {
        return djangoApiService.obtenerEstadisticasPanel();
    }
    
    @GetMapping("/solicitudes-pendientes")
    public DjangoApiResponse obtenerSolicitudesPendientes() {
        return djangoApiService.obtenerSolicitudesPendientes();
    }
    
    @GetMapping("/test-conexion")
    public DjangoApiResponse testConexion() {
        try {
            DjangoApiResponse response = djangoApiService.obtenerEstadisticasPanel();
            if (response.isSuccess()) {
                response.setMessage("Conexión con Django API establecida correctamente");
            }
            return response;
        } catch (Exception e) {
            return new DjangoApiResponse(false, "Error de conexión: " + e.getMessage());
        }
    }
}