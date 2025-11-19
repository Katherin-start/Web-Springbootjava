package com.parkea.ya.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "datos_movilidad_urbana")
public class UrbanMobility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_analisis", nullable = false)
    private LocalDate fechaAnalisis;

    @Column(name = "zona_urbana", nullable = false, length = 100)
    private String zonaUrbana;

    @Column(name = "total_vehiculos")
    private Integer totalVehiculos;

    @Column(name = "vehiculos_circulando")
    private Integer vehiculosCirculando;

    @Column(name = "nivel_trafico")
    private Double nivelTrafico; // Porcentaje 0-100

    @Column(name = "nivel_congestion")
    private Double nivelCongestion; // Porcentaje 0-100

    @Column(name = "velocidad_promedio")
    private Double velocidadPromedio; // km/h

    @Column(name = "tiempo_viaje_promedio")
    private Integer tiempoViajePromedio; // minutos

    @Column(name = "demanda_estacionamiento")
    private Double demandaEstacionamiento; // Porcentaje 0-100

    @Column(name = "disponibilidad_estacionamiento")
    private Double disponibilidadEstacionamiento; // Porcentaje 0-100

    @Column(name = "ocupacion_estacionamientos")
    private Double ocupacionEstacionamientos; // Porcentaje 0-100

    @Column(name = "emisiones_co2")
    private Double emisionesCO2; // kg CO2 por hora

    @Column(name = "reduccion_emisiones_potencial")
    private Double reduccionEmisionesPotencial; // Porcentaje

    @Column(name = "uso_transporte_publico")
    private Double usoTransportePublico; // Porcentaje

    @Column(name = "uso_bicicletas")
    private Double usoBicicletas; // Porcentaje

    @Column(name = "peatones")
    private Integer peatones;

    @Column(name = "eventos_especiales")
    private String eventosEspeciales; // JSON o descripción

    @Column(name = "condiciones_climaticas", length = 50)
    private String condicionesClimaticas;

    @Column(name = "impacto_estacionamientos_inteligentes")
    private Double impactoEstacionamientosInteligentes; // Porcentaje mejora

    @Column(name = "ahorro_tiempo_promedio")
    private Integer ahorroTiempoPromedio; // minutos por viaje

    @Column(name = "reduccion_busqueda_estacionamiento")
    private Double reduccionBusquedaEstacionamiento; // Porcentaje

    @Column(name = "satisfaccion_usuarios")
    private Double satisfaccionUsuarios; // Porcentaje 0-100

    @Column(name = "indice_movilidad_sostenible")
    private Double indiceMovilidadSostenible; // 0-100

    @Column(name = "prediccion_demanda_24h")
    private String prediccionDemanda24h; // JSON con predicciones

    @Column(name = "recomendaciones_ia", length = 1000)
    private String recomendacionesIA;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fuente_datos", length = 100)
    private String fuenteDatos;

    // Constructores
    public UrbanMobility() {
        this.fechaActualizacion = LocalDateTime.now();
        this.fechaAnalisis = LocalDate.now();
    }

    public UrbanMobility(String zonaUrbana, LocalDate fechaAnalisis) {
        this();
        this.zonaUrbana = zonaUrbana;
        this.fechaAnalisis = fechaAnalisis;
    }

    // Métodos de negocio
    public void calcularIndicadores() {
        // Calcular nivel de congestión basado en tráfico y velocidad
        if (velocidadPromedio != null && velocidadPromedio > 0) {
            this.nivelCongestion = Math.max(0, Math.min(100, 100 - (velocidadPromedio / 80.0 * 100)));
        }

        // Calcular índice de movilidad sostenible
        double sostenibilidad = 0.0;
        int factores = 0;

        if (usoTransportePublico != null) {
            sostenibilidad += usoTransportePublico;
            factores++;
        }
        if (usoBicicletas != null) {
            sostenibilidad += usoBicicletas * 1.5; // Las bicicletas tienen mayor peso
            factores++;
        }
        if (reduccionEmisionesPotencial != null) {
            sostenibilidad += reduccionEmisionesPotencial;
            factores++;
        }

        this.indiceMovilidadSostenible = factores > 0 ? sostenibilidad / factores : null;
    }

    public void actualizarImpactoEstacionamientos(Double mejoraTrafico, Double reduccionEmisiones, Double ahorroTiempo) {
        this.impactoEstacionamientosInteligentes = (mejoraTrafico + reduccionEmisiones + ahorroTiempo) / 3.0;
        this.reduccionBusquedaEstacionamiento = ahorroTiempo;
        this.reduccionEmisionesPotencial = reduccionEmisiones;
        this.ahorroTiempoPromedio = ahorroTiempo != null ? (int)(ahorroTiempo * 60) : null; // Convertir a minutos
        this.fechaActualizacion = LocalDateTime.now();
    }

    public boolean esZonaCongestionada() {
        return nivelCongestion != null && nivelCongestion > 70;
    }

    public boolean tieneAltaDemandaEstacionamiento() {
        return demandaEstacionamiento != null && demandaEstacionamiento > 80;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaAnalisis() { return fechaAnalisis; }
    public void setFechaAnalisis(LocalDate fechaAnalisis) { this.fechaAnalisis = fechaAnalisis; }

    public String getZonaUrbana() { return zonaUrbana; }
    public void setZonaUrbana(String zonaUrbana) { this.zonaUrbana = zonaUrbana; }

    public Integer getTotalVehiculos() { return totalVehiculos; }
    public void setTotalVehiculos(Integer totalVehiculos) { this.totalVehiculos = totalVehiculos; }

    public Integer getVehiculosCirculando() { return vehiculosCirculando; }
    public void setVehiculosCirculando(Integer vehiculosCirculando) { this.vehiculosCirculando = vehiculosCirculando; }

    public Double getNivelTrafico() { return nivelTrafico; }
    public void setNivelTrafico(Double nivelTrafico) { this.nivelTrafico = nivelTrafico; }

    public Double getNivelCongestion() { return nivelCongestion; }
    public void setNivelCongestion(Double nivelCongestion) { this.nivelCongestion = nivelCongestion; }

    public Double getVelocidadPromedio() { return velocidadPromedio; }
    public void setVelocidadPromedio(Double velocidadPromedio) { this.velocidadPromedio = velocidadPromedio; }

    public Integer getTiempoViajePromedio() { return tiempoViajePromedio; }
    public void setTiempoViajePromedio(Integer tiempoViajePromedio) { this.tiempoViajePromedio = tiempoViajePromedio; }

    public Double getDemandaEstacionamiento() { return demandaEstacionamiento; }
    public void setDemandaEstacionamiento(Double demandaEstacionamiento) { this.demandaEstacionamiento = demandaEstacionamiento; }

    public Double getDisponibilidadEstacionamiento() { return disponibilidadEstacionamiento; }
    public void setDisponibilidadEstacionamiento(Double disponibilidadEstacionamiento) { this.disponibilidadEstacionamiento = disponibilidadEstacionamiento; }

    public Double getOcupacionEstacionamientos() { return ocupacionEstacionamientos; }
    public void setOcupacionEstacionamientos(Double ocupacionEstacionamientos) { this.ocupacionEstacionamientos = ocupacionEstacionamientos; }

    public Double getEmisionesCO2() { return emisionesCO2; }
    public void setEmisionesCO2(Double emisionesCO2) { this.emisionesCO2 = emisionesCO2; }

    public Double getReduccionEmisionesPotencial() { return reduccionEmisionesPotencial; }
    public void setReduccionEmisionesPotencial(Double reduccionEmisionesPotencial) { this.reduccionEmisionesPotencial = reduccionEmisionesPotencial; }

    public Double getUsoTransportePublico() { return usoTransportePublico; }
    public void setUsoTransportePublico(Double usoTransportePublico) { this.usoTransportePublico = usoTransportePublico; }

    public Double getUsoBicicletas() { return usoBicicletas; }
    public void setUsoBicicletas(Double usoBicicletas) { this.usoBicicletas = usoBicicletas; }

    public Integer getPeatones() { return peatones; }
    public void setPeatones(Integer peatones) { this.peatones = peatones; }

    public String getEventosEspeciales() { return eventosEspeciales; }
    public void setEventosEspeciales(String eventosEspeciales) { this.eventosEspeciales = eventosEspeciales; }

    public String getCondicionesClimaticas() { return condicionesClimaticas; }
    public void setCondicionesClimaticas(String condicionesClimaticas) { this.condicionesClimaticas = condicionesClimaticas; }

    public Double getImpactoEstacionamientosInteligentes() { return impactoEstacionamientosInteligentes; }
    public void setImpactoEstacionamientosInteligentes(Double impactoEstacionamientosInteligentes) { this.impactoEstacionamientosInteligentes = impactoEstacionamientosInteligentes; }

    public Integer getAhorroTiempoPromedio() { return ahorroTiempoPromedio; }
    public void setAhorroTiempoPromedio(Integer ahorroTiempoPromedio) { this.ahorroTiempoPromedio = ahorroTiempoPromedio; }

    public Double getReduccionBusquedaEstacionamiento() { return reduccionBusquedaEstacionamiento; }
    public void setReduccionBusquedaEstacionamiento(Double reduccionBusquedaEstacionamiento) { this.reduccionBusquedaEstacionamiento = reduccionBusquedaEstacionamiento; }

    public Double getSatisfaccionUsuarios() { return satisfaccionUsuarios; }
    public void setSatisfaccionUsuarios(Double satisfaccionUsuarios) { this.satisfaccionUsuarios = satisfaccionUsuarios; }

    public Double getIndiceMovilidadSostenible() { return indiceMovilidadSostenible; }
    public void setIndiceMovilidadSostenible(Double indiceMovilidadSostenible) { this.indiceMovilidadSostenible = indiceMovilidadSostenible; }

    public String getPrediccionDemanda24h() { return prediccionDemanda24h; }
    public void setPrediccionDemanda24h(String prediccionDemanda24h) { this.prediccionDemanda24h = prediccionDemanda24h; }

    public String getRecomendacionesIA() { return recomendacionesIA; }
    public void setRecomendacionesIA(String recomendacionesIA) { this.recomendacionesIA = recomendacionesIA; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getFuenteDatos() { return fuenteDatos; }
    public void setFuenteDatos(String fuenteDatos) { this.fuenteDatos = fuenteDatos; }
}