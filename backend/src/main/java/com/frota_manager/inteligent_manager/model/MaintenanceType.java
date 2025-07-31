package com.frota_manager.inteligent_manager.model;

/**
 * Enum que representa os diferentes tipos de manutenção
 * Utilizado para categorizar as manutenções por tipo
 */
public enum MaintenanceType {
    PREVENTIVE("Preventiva"),
    CORRECTIVE("Corretiva"),
    INSPECTION("Inspeção"),
    REPAIR("Reparação"),
    REPLACEMENT("Substituição"),
    EMERGENCY("Emergência");
    
    private final String description;
    
    MaintenanceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 