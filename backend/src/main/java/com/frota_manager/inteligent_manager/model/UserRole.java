package com.frota_manager.inteligent_manager.model;

/**
 * Enum para roles de usuário no sistema
 */
public enum UserRole {
    ADMIN("Administrador"),
    MANAGER("Gerente"),
    DRIVER("Condutor"),
    MECHANIC("Mecânico"),
    VIEWER("Visualizador");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 