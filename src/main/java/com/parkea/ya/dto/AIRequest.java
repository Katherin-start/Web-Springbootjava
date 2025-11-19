package com.parkea.ya.dto;

public class AIRequest {
    private String prompt;
    private String tipo; // "vision", "beneficios", "prediccion", "personalizado"
    private String parametros; // JSON adicional si es necesario
    
    // Constructores, getters y setters
    public AIRequest() {}
    
    public AIRequest(String prompt) {
        this.prompt = prompt;
    }
    
    public AIRequest(String prompt, String tipo) {
        this.prompt = prompt;
        this.tipo = tipo;
    }
    
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }
}