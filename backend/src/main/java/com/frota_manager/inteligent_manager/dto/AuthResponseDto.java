package com.frota_manager.inteligent_manager.dto;

/**
 * DTO para resposta de autenticação
 */
public record AuthResponseDto(
    String token,
    String username,
    String message
) {} 