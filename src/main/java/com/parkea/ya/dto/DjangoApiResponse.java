package com.parkea.ya.dto;

import java.util.Map;

public class DjangoApiResponse {
    private boolean success;
    private String message;
    private Map<String, Object> data;
    private Integer statusCode;
    
    // Constructores
    public DjangoApiResponse() {}
    
    public DjangoApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Getters y Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
}