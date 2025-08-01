package com.frota_manager.inteligent_manager.model;

/**
 * Enum para tipos de notificação no sistema
 */
public enum NotificationType {
    MAINTENANCE_DUE("Manutenção Vencida"),
    MAINTENANCE_UPCOMING("Manutenção Próxima"),
    MAINTENANCE_OVERDUE("Manutenção Atrasada"),
    LICENSE_EXPIRING("Licença Expirando"),
    LICENSE_EXPIRED("Licença Vencida"),
    LOW_FUEL("Combustível Baixo"),
    TRIP_COMPLETED("Viagem Concluída"),
    TRIP_DELAYED("Viagem Atrasada"),
    VEHICLE_BREAKDOWN("Avaria do Veículo"),
    VEHICLE_INSPECTION_DUE("Inspeção Vencida"),
    COST_THRESHOLD_EXCEEDED("Limite de Custo Excedido"),
    SYSTEM_ALERT("Alerta do Sistema"),
    GENERAL_INFO("Informação Geral");
    
    private final String displayName;
    
    NotificationType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}