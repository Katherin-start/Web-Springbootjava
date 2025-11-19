package com.parkea.ya.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "datos_estacionamiento")
public class ParkingData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_estacionamiento", nullable = false, length = 100)
    private String nombreEstacionamiento;

    @Column(name = "direccion", length = 200)
    private String direccion;

    // GEOLOCALIZACIÓN - CORREGIDO
    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "total_plazas")
    private Integer totalPlazas;

    @Column(name = "plazas_disponibles")
    private Integer plazasDisponibles;

    @Column(name = "plazas_ocupadas")
    private Integer plazasOcupadas;

    // TARIFAS - CORREGIDO
    @Column(name = "tarifa_hora")
    private Double tarifaHora;

    @Column(name = "tarifa_dia")
    private Double tarifaDia;

    @Column(name = "nivel_ocupacion")
    private Double nivelOcupacion; // Porcentaje 0-100

    @Column(name = "hora_pico_entrada")
    private LocalTime horaPicoEntrada;

    @Column(name = "hora_pico_salida")
    private LocalTime horaPicoSalida;

    @Column(name = "tiempo_promedio_estadia")
    private Integer tiempoPromedioEstadia; // En minutos

    // INGRESOS - CORREGIDO
    @Column(name = "ingresos_diarios")
    private Double ingresosDiarios;

    @Column(name = "ingresos_semanales")
    private Double ingresosSemanales;

    @Column(name = "ingresos_mensuales")
    private Double ingresosMensuales;

    // RATING - CORREGIDO
    @Column(name = "rating_promedio")
    private Double ratingPromedio;

    @Column(name = "total_resenas")
    private Integer totalResenas;

    @Column(name = "nivel_seguridad", length = 50)
    private String nivelSeguridad; // "ALTA", "MEDIA", "BAJA"

    @Column(name = "servicios", length = 500)
    private String servicios; // JSON o lista separada por comas

    @Column(name = "horario_apertura")
    private LocalTime horarioApertura;

    @Column(name = "horario_cierre")
    private LocalTime horarioCierre;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "tipo_estacionamiento", length = 50)
    private String tipoEstacionamiento; // "PUBLICO", "PRIVADO", "RESIDENCIAL", "COMERCIAL"

    @Column(name = "zona_urbana", length = 100)
    private String zonaUrbana;

    @Column(name = "demanda_promedio")
    private Double demandaPromedio; // Porcentaje 0-100

    @Column(name = "tasa_rotacion")
    private Double tasaRotacion; // Vehículos por hora

    // Constructores (igual que antes)
    public ParkingData() {
        this.fechaActualizacion = LocalDateTime.now();
        this.activo = true;
    }

    public ParkingData(String nombreEstacionamiento, String direccion, Double latitud, Double longitud, 
                      Integer totalPlazas, Double tarifaHora) {
        this();
        this.nombreEstacionamiento = nombreEstacionamiento;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.totalPlazas = totalPlazas;
        this.plazasDisponibles = totalPlazas;
        this.plazasOcupadas = 0;
        this.tarifaHora = tarifaHora;
        this.nivelOcupacion = 0.0;
    }

    // ... (métodos de negocio y getters/setters IGUALES)


    // Métodos de negocio
    public void actualizarOcupacion(Integer plazasOcupadas) {
        this.plazasOcupadas = plazasOcupadas;
        this.plazasDisponibles = this.totalPlazas - plazasOcupadas;
        this.nivelOcupacion = (double) plazasOcupadas / this.totalPlazas * 100;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void agregarResena(Double rating) {
        if (this.ratingPromedio == null) {
            this.ratingPromedio = rating;
            this.totalResenas = 1;
        } else {
            double totalRating = this.ratingPromedio * this.totalResenas;
            this.totalResenas++;
            this.ratingPromedio = (totalRating + rating) / this.totalResenas;
        }
    }

    public boolean tieneDisponibilidad() {
        return this.plazasDisponibles != null && this.plazasDisponibles > 0;
    }

    public boolean estaAbierto() {
        if (horarioApertura == null || horarioCierre == null) return true;
        
        LocalTime ahora = LocalTime.now();
        return !ahora.isBefore(horarioApertura) && !ahora.isAfter(horarioCierre);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreEstacionamiento() { return nombreEstacionamiento; }
    public void setNombreEstacionamiento(String nombreEstacionamiento) { this.nombreEstacionamiento = nombreEstacionamiento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Integer getTotalPlazas() { return totalPlazas; }
    public void setTotalPlazas(Integer totalPlazas) { this.totalPlazas = totalPlazas; }

    public Integer getPlazasDisponibles() { return plazasDisponibles; }
    public void setPlazasDisponibles(Integer plazasDisponibles) { this.plazasDisponibles = plazasDisponibles; }

    public Integer getPlazasOcupadas() { return plazasOcupadas; }
    public void setPlazasOcupadas(Integer plazasOcupadas) { this.plazasOcupadas = plazasOcupadas; }

    public Double getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(Double tarifaHora) { this.tarifaHora = tarifaHora; }

    public Double getTarifaDia() { return tarifaDia; }
    public void setTarifaDia(Double tarifaDia) { this.tarifaDia = tarifaDia; }

    public Double getNivelOcupacion() { return nivelOcupacion; }
    public void setNivelOcupacion(Double nivelOcupacion) { this.nivelOcupacion = nivelOcupacion; }

    public LocalTime getHoraPicoEntrada() { return horaPicoEntrada; }
    public void setHoraPicoEntrada(LocalTime horaPicoEntrada) { this.horaPicoEntrada = horaPicoEntrada; }

    public LocalTime getHoraPicoSalida() { return horaPicoSalida; }
    public void setHoraPicoSalida(LocalTime horaPicoSalida) { this.horaPicoSalida = horaPicoSalida; }

    public Integer getTiempoPromedioEstadia() { return tiempoPromedioEstadia; }
    public void setTiempoPromedioEstadia(Integer tiempoPromedioEstadia) { this.tiempoPromedioEstadia = tiempoPromedioEstadia; }

    public Double getIngresosDiarios() { return ingresosDiarios; }
    public void setIngresosDiarios(Double ingresosDiarios) { this.ingresosDiarios = ingresosDiarios; }

    public Double getIngresosSemanales() { return ingresosSemanales; }
    public void setIngresosSemanales(Double ingresosSemanales) { this.ingresosSemanales = ingresosSemanales; }

    public Double getIngresosMensuales() { return ingresosMensuales; }
    public void setIngresosMensuales(Double ingresosMensuales) { this.ingresosMensuales = ingresosMensuales; }

    public Double getRatingPromedio() { return ratingPromedio; }
    public void setRatingPromedio(Double ratingPromedio) { this.ratingPromedio = ratingPromedio; }

    public Integer getTotalResenas() { return totalResenas; }
    public void setTotalResenas(Integer totalResenas) { this.totalResenas = totalResenas; }

    public String getNivelSeguridad() { return nivelSeguridad; }
    public void setNivelSeguridad(String nivelSeguridad) { this.nivelSeguridad = nivelSeguridad; }

    public String getServicios() { return servicios; }
    public void setServicios(String servicios) { this.servicios = servicios; }

    public LocalTime getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(LocalTime horarioApertura) { this.horarioApertura = horarioApertura; }

    public LocalTime getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(LocalTime horarioCierre) { this.horarioCierre = horarioCierre; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getTipoEstacionamiento() { return tipoEstacionamiento; }
    public void setTipoEstacionamiento(String tipoEstacionamiento) { this.tipoEstacionamiento = tipoEstacionamiento; }

    public String getZonaUrbana() { return zonaUrbana; }
    public void setZonaUrbana(String zonaUrbana) { this.zonaUrbana = zonaUrbana; }

    public Double getDemandaPromedio() { return demandaPromedio; }
    public void setDemandaPromedio(Double demandaPromedio) { this.demandaPromedio = demandaPromedio; }

    public Double getTasaRotacion() { return tasaRotacion; }
    public void setTasaRotacion(Double tasaRotacion) { this.tasaRotacion = tasaRotacion; }
}