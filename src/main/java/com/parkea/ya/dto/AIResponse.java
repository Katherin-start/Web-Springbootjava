package com.parkea.ya.dto;

public class AIResponse {
    private boolean success;
    private String contenido;
    private String error;
    private String tipo;
    private long timestamp;
    
    public AIResponse() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public AIResponse(boolean success, String contenido) {
        this();
        this.success = success;
        this.contenido = contenido;
    }
    
    public AIResponse(boolean success, String contenido, String tipo) {
        this();
        this.success = success;
        this.contenido = contenido;
        this.tipo = tipo;
    }
    
    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}