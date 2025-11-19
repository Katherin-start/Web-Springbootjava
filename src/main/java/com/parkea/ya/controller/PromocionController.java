package com.parkea.ya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.parkea.ya.dto.DjangoApiResponse;
import com.parkea.ya.dto.ParkingRegistrationRequest;
import com.parkea.ya.service.DjangoApiService;

@Controller
public class PromocionController {
    
    @Autowired
    private DjangoApiService djangoApiService;

    @GetMapping("/solicitar-acceso")
    public String solicitarAcceso(Model model) {
        model.addAttribute("titulo", "Solicitar Acceso al Panel - Parkea Ya");
        return "pages/promocion";
    }

    @PostMapping("/enviar-solicitud")
    public String enviarSolicitud(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam(required = false) String empresa,
            @RequestParam(required = false) String mensaje,
            Model model) {
        
        // Crear DTO para la solicitud
        ParkingRegistrationRequest solicitud = new ParkingRegistrationRequest(
            nombre, email, telefono, empresa, mensaje
        );
        
        // Enviar a Django API
        DjangoApiResponse respuesta = djangoApiService.enviarSolicitudAcceso(solicitud);
        
        if (respuesta.isSuccess()) {
            model.addAttribute("titulo", "Solicitud Enviada - Parkea Ya");
            model.addAttribute("nombre", nombre);
            model.addAttribute("email", email);
            model.addAttribute("mensaje", "¡Gracias por tu interés! Tu solicitud ha sido enviada al panel de administración.");
        } else {
            model.addAttribute("titulo", "Error en Solicitud - Parkea Ya");
            model.addAttribute("mensaje", "Hubo un error al enviar tu solicitud: " + respuesta.getMessage());
        }
        
        return "pages/confirmacion";
    }
}