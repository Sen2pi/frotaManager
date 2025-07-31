package com.frota_manager.inteligent_manager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de registro de usuário
 */
public record RegisterRequestDto(
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    String name,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    String email,
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    String password,
    
    @NotBlank(message = "Confirmação de senha é obrigatória")
    String confirmPassword,
    
    @NotBlank(message = "Cargo é obrigatório")
    String role
) {} 