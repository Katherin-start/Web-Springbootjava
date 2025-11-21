package com.parkea.ya.dto;

public class ParkingRegistrationRequest {
    private String nombre;
    private String email;
    private String telefono;
    private String empresa;
    private String mensaje;
    
    // Constructores
    public ParkingRegistrationRequest() {}
    
    public ParkingRegistrationRequest(String nombre, String email, String telefono, String empresa, String mensaje) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.empresa = empresa;
        this.mensaje = mensaje;
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}