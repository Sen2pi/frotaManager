package com.frota_manager.inteligent_manager.model;

/**
 * Enum para prioridade de notificações
 */
public enum NotificationPriority {
    LOW("Baixa"),
    MEDIUM("Média"),
    HIGH("Alta"),
    CRITICAL("Crítica");
    
    private final String displayName;
    
    NotificationPriority(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}