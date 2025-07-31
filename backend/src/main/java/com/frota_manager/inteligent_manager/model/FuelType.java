package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes tipos de combustível
 * Utilizado para categorizar os veículos por tipo de combustível
 */
public enum FuelType {
    GASOLINE("Gasolina"),
    DIESEL("Gasóleo"),
    ELECTRIC("Elétrico"),
    HYBRID("Híbrido"),
    LPG("GPL"),
    CNG("Gás Natural");
    
    private final String description;
    
    FuelType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 