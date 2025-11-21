package com.parkea.ya.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "solicitudes_acceso")
public class SolicitudAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre_completo", nullable = false, length = 100)
    private String nombreCompleto;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Size(max = 100, message = "El nombre de la empresa no puede exceder 100 caracteres")
    @Column(name = "empresa", length = 100)
    private String empresa;

    @Size(max = 500, message = "El mensaje no puede exceder 500 caracteres")
    @Column(name = "mensaje", length = 500)
    private String mensaje;

    // GEOLOCALIZACIÓN - CORREGIDO SIN PRECISION/SCALE
    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "direccion_completa", length = 255)
    private String direccionCompleta;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    @Column(name = "notas_administrador", length = 500)
    private String notasAdministrador;

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    @Column(name = "tipo_solicitud", length = 50)
    private String tipoSolicitud = "ACCESO_PANEL";

    @Column(name = "ip_solicitud", length = 45)
    private String ipSolicitud;

    // Constructores
    public SolicitudAcceso() {
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
        this.tipoSolicitud = "ACCESO_PANEL";
    }

    public SolicitudAcceso(String nombreCompleto, String email, String telefono, String empresa, String mensaje) {
        this();
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.empresa = empresa;
        this.mensaje = mensaje;
    }

    public SolicitudAcceso(String nombreCompleto, String email, String telefono, String empresa, String mensaje, 
                          Double latitud, Double longitud, String direccionCompleta, String ciudad) {
        this(nombreCompleto, email, telefono, empresa, mensaje);
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccionCompleta = direccionCompleta;
        this.ciudad = ciudad;
    }

    // Enums para el estado
    public enum EstadoSolicitud {
        PENDIENTE,
        EN_REVISION,
        APROBADA,
        RECHAZADA,
        CANCELADA
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getDireccionCompleta() { return direccionCompleta; }
    public void setDireccionCompleta(String direccionCompleta) { this.direccionCompleta = direccionCompleta; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public String getNotasAdministrador() { return notasAdministrador; }
    public void setNotasAdministrador(String notasAdministrador) { this.notasAdministrador = notasAdministrador; }

    public LocalDateTime getFechaRevision() { return fechaRevision; }
    public void setFechaRevision(LocalDateTime fechaRevision) { this.fechaRevision = fechaRevision; }

    public String getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(String tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }

    public String getIpSolicitud() { return ipSolicitud; }
    public void setIpSolicitud(String ipSolicitud) { this.ipSolicitud = ipSolicitud; }

    // Métodos de utilidad
    public void aprobar(String notas) {
        this.estado = EstadoSolicitud.APROBADA;
        this.notasAdministrador = notas;
        this.fechaRevision = LocalDateTime.now();
    }

    public void rechazar(String notas) {
        this.estado = EstadoSolicitud.RECHAZADA;
        this.notasAdministrador = notas;
        this.fechaRevision = LocalDateTime.now();
    }

    public boolean isPendiente() {
        return EstadoSolicitud.PENDIENTE.equals(this.estado);
    }

    @Override
    public String toString() {
        return "SolicitudAcceso{" +
                "id=" + id +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                ", fechaSolicitud=" + fechaSolicitud +
                '}';
    }
}