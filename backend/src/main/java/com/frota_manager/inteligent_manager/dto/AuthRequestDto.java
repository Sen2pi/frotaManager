package com.frota_manager.inteligent_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de autenticação (login)
 */
public record AuthRequestDto(
    @NotBlank(message = "Email é obrigatório")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    String password
) {} 