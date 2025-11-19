package com.parkea.ya.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parkea.ya.dto.AIRequest;
import com.parkea.ya.dto.AIResponse;
import com.parkea.ya.model.ParkingData;
import com.parkea.ya.model.UrbanMobility;
import com.parkea.ya.repository.ParkingDataRepository;
import com.parkea.ya.repository.UrbanMobilityRepository;

@Service
public class ParkingAnalysisService {
    
    @Autowired
    private ParkingDataRepository parkingDataRepository;
    
    @Autowired
    private UrbanMobilityRepository urbanMobilityRepository;
    
    @Autowired
    private GeminiAIService geminiAIService;
    
    public Map<String, Object> analizarDemandaZona(String zona) {
        Map<String, Object> analisis = new HashMap<>();
        
        try {
            // Obtener datos de estacionamientos en la zona
            List<ParkingData> parkings = parkingDataRepository.findByZonaUrbana(zona);
            Optional<UrbanMobility> movilidad = urbanMobilityRepository.findFirstByZonaUrbanaOrderByFechaAnalisisDesc(zona);
            
            // Calcular métricas
            double ocupacionPromedio = parkings.stream()
                .filter(p -> p.getNivelOcupacion() != null)
                .mapToDouble(ParkingData::getNivelOcupacion)
                .average()
                .orElse(0.0);
            
            double disponibilidadPromedio = parkings.stream()
                .filter(p -> p.getPlazasDisponibles() != null && p.getTotalPlazas() != null && p.getTotalPlazas() > 0)
                .mapToDouble(p -> (double) p.getPlazasDisponibles() / p.getTotalPlazas() * 100)
                .average()
                .orElse(0.0);
            
            // Determinar estado de la demanda
            String estadoDemanda;
            if (ocupacionPromedio > 80) estadoDemanda = "ALTA";
            else if (ocupacionPromedio > 50) estadoDemanda = "MEDIA";
            else estadoDemanda = "BAJA";
            
            // Horas pico detectadas
            List<String> horasPico = detectarHorasPico(parkings);
            
            analisis.put("zona", zona);
            analisis.put("totalEstacionamientos", parkings.size());
            analisis.put("ocupacionPromedio", Math.round(ocupacionPromedio * 100.0) / 100.0);
            analisis.put("disponibilidadPromedio", Math.round(disponibilidadPromedio * 100.0) / 100.0);
            analisis.put("estadoDemanda", estadoDemanda);
            analisis.put("horasPico", horasPico);
            analisis.put("timestamp", LocalDateTime.now());
            
            // Agregar datos de movilidad si están disponibles
            movilidad.ifPresent(m -> {
                analisis.put("nivelTrafico", m.getNivelTrafico());
                analisis.put("nivelCongestion", m.getNivelCongestion());
                analisis.put("velocidadPromedio", m.getVelocidadPromedio());
            });
            
        } catch (Exception e) {
            analisis.put("error", "Error en el análisis: " + e.getMessage());
        }
        
        return analisis;
    }
    
    public Map<String, Object> predecirDemandaFutura(String zona, int horas) {
        Map<String, Object> prediccion = new HashMap<>();
        
        try {
            // Simulación de predicción basada en datos históricos
            List<ParkingData> datosHistoricos = parkingDataRepository.findByZonaUrbana(zona);
            
            // Algoritmo simple de predicción (en un sistema real usarías ML)
            double factorHoraPico = esHoraPico() ? 1.3 : 0.8;
            double factorDiaSemana = esDiaLaboral() ? 1.2 : 0.9;
            double factorBase = 65.0; // Demanda base del 65%
            
            double demandaPredicha = factorBase * factorHoraPico * factorDiaSemana;
            demandaPredicha = Math.min(100, Math.max(0, demandaPredicha));
            
            String tendencia = demandaPredicha > 70 ? "CRECIENTE" : 
                              demandaPredicha < 40 ? "DECRECIENTE" : "ESTABLE";
            
            // Recomendaciones basadas en la predicción
            List<String> recomendaciones = generarRecomendaciones(demandaPredicha, zona);
            
            prediccion.put("zona", zona);
            prediccion.put("demandaPredicha", Math.round(demandaPredicha * 100.0) / 100.0);
            prediccion.put("horizontePrediccion", horas + " horas");
            prediccion.put("tendencia", tendencia);
            prediccion.put("recomendaciones", recomendaciones);
            prediccion.put("confianza", 85); // Porcentaje de confianza
            prediccion.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            prediccion.put("error", "Error en la predicción: " + e.getMessage());
        }
        
        return prediccion;
    }
    
    public Map<String, Object> calcularImpactoAmbiental(String zona) {
        Map<String, Object> impacto = new HashMap<>();
        
        try {
            List<ParkingData> parkings = parkingDataRepository.findByZonaUrbana(zona);
            Optional<UrbanMobility> movilidad = urbanMobilityRepository.findFirstByZonaUrbanaOrderByFechaAnalisisDesc(zona);
            
            // Cálculos simplificados de impacto ambiental
            int totalPlazas = parkings.stream()
                .mapToInt(p -> p.getTotalPlazas() != null ? p.getTotalPlazas() : 0)
                .sum();
            
            // Suposiciones para el cálculo
            double reduccionBusqueda = 15.0; // 15% de reducción en tiempo de búsqueda
            double emisionesPorVehiculoHora = 2.5; // kg CO2 por vehículo por hora
            double vehiculosAfectados = totalPlazas * 0.3; // 30% de los vehículos se benefician
            
            double ahorroEmisiones = reduccionBusqueda / 100.0 * emisionesPorVehiculoHora * vehiculosAfectados;
            double ahorroCombustible = ahorroEmisiones * 0.4; // Relación aproximada CO2-combustible
            
            impacto.put("zona", zona);
            impacto.put("totalPlazasAnalizadas", totalPlazas);
            impacto.put("reduccionTiempoBusqueda", reduccionBusqueda + "%");
            impacto.put("ahorroEmisionesCO2", Math.round(ahorroEmisiones * 100.0) / 100.0 + " kg/hora");
            impacto.put("ahorroCombustible", Math.round(ahorroCombustible * 100.0) / 100.0 + " L/hora");
            impacto.put("vehiculosBeneficiados", (int) vehiculosAfectados);
            impacto.put("equivalenteArboles", (int) (ahorroEmisiones / 20.0)); // 1 árbol absorbe ~20kg CO2/año
            
            // Agregar datos de movilidad si están disponibles
            movilidad.ifPresent(m -> {
                impacto.put("emisionesActuales", m.getEmisionesCO2());
                impacto.put("reduccionPotencial", m.getReduccionEmisionesPotencial());
            });
            
        } catch (Exception e) {
            impacto.put("error", "Error en el cálculo de impacto: " + e.getMessage());
        }
        
        return impacto;
    }
    
    public String generarReporteIA(String zona) {
        try {
            // Obtener análisis actual
            Map<String, Object> analisis = analizarDemandaZona(zona);
            Map<String, Object> prediccion = predecirDemandaFutura(zona, 24);
            Map<String, Object> impacto = calcularImpactoAmbiental(zona);
            
            // Construir prompt para IA
            String prompt = String.format(
                "Genera un reporte ejecutivo sobre la situación de estacionamientos en %s. " +
                "Datos actuales: Ocupación %.1f%%, Demanda %s. " +
                "Predicción: %.1f%% de demanda en 24 horas, tendencia %s. " +
                "Impacto ambiental: %s de CO2 ahorrado por hora. " +
                "Proporciona 3 recomendaciones clave para optimizar la movilidad en esta zona. " +
                "Máximo 200 palabras. Responde en español.",
                zona,
                analisis.get("ocupacionPromedio"),
                analisis.get("estadoDemanda"),
                prediccion.get("demandaPredicha"),
                prediccion.get("tendencia"),
                impacto.get("ahorroEmisionesCO2")
            );
            
            AIRequest request = new AIRequest(prompt, "analisis");
            AIResponse response = geminiAIService.generarContenido(request);
            
            return response.isSuccess() ? response.getContenido() : "Error al generar reporte con IA";
            
        } catch (Exception e) {
            return "Error generando reporte: " + e.getMessage();
        }
    }
    
    // Métodos auxiliares privados
    private List<String> detectarHorasPico(List<ParkingData> parkings) {
        // Simulación de detección de horas pico
        return Arrays.asList("08:00-10:00", "17:00-19:00");
    }
    
    private boolean esHoraPico() {
        LocalTime ahora = LocalTime.now();
        return (ahora.isAfter(LocalTime.of(7, 0)) && ahora.isBefore(LocalTime.of(10, 0))) ||
               (ahora.isAfter(LocalTime.of(16, 0)) && ahora.isBefore(LocalTime.of(19, 0)));
    }
    
    private boolean esDiaLaboral() {
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_WEEK);
        return dia >= Calendar.MONDAY && dia <= Calendar.FRIDAY;
    }
    
    private List<String> generarRecomendaciones(double demanda, String zona) {
        List<String> recomendaciones = new ArrayList<>();
        
        if (demanda > 80) {
            recomendaciones.add("Activar tarifas dinámicas para gestionar la demanda");
            recomendaciones.add("Promover el uso de transporte alternativo en " + zona);
            recomendaciones.add("Habilitar estacionamientos temporales cercanos");
        } else if (demanda < 40) {
            recomendaciones.add("Ofrecer promociones para aumentar la ocupación");
            recomendaciones.add("Coordinar con eventos locales para atraer más vehículos");
            recomendaciones.add("Optimizar horarios de operación");
        } else {
            recomendaciones.add("Mantener monitoreo continuo de la demanda");
            recomendaciones.add("Preparar planes para picos de demanda inesperados");
            recomendaciones.add("Comunicar disponibilidad actual a los usuarios");
        }
        
        return recomendaciones;
    }
}