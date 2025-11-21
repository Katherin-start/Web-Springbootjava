package com.parkea.ya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Parkea Ya - Estacionamiento Inteligente");
        model.addAttribute("mensaje", "Bienvenido al futuro de la movilidad urbana");
        model.addAttribute("paginaActiva", "inicio"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/index";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model) {
        model.addAttribute("titulo", "Beneficios - Parkea Ya");
        model.addAttribute("paginaActiva", "beneficios"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/beneficios";
    }

    @GetMapping("/futuro-movilidad")
    public String futuroMovilidad(Model model) {
        model.addAttribute("titulo", "Futuro de la Movilidad - Parkea Ya");
        model.addAttribute("paginaActiva", "futuro-movilidad"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/futuro-movilidad";
    }

    @GetMapping("/ia-generador")
    public String iaGenerador(Model model) {
        model.addAttribute("titulo", "Generador IA - Parkea Ya");
        model.addAttribute("paginaActiva", "ia-generador"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/ia-generador";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("titulo", "Contacto - Parkea Ya");
        model.addAttribute("paginaActiva", "contacto"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/contacto";
    }

    @GetMapping("/promocion")
    public String promocion(Model model) {
        model.addAttribute("titulo", "Solicitar Acceso - Parkea Ya");
        model.addAttribute("paginaActiva", "promocion"); // ⭐ AGREGAR ESTA LÍNEA
        return "pages/promocion";
    }
}