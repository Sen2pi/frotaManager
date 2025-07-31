package com.frota_manager.inteligent_manager.controller;

import com.frota_manager.inteligent_manager.dto.AuthRequestDto;
import com.frota_manager.inteligent_manager.dto.AuthResponseDto;
import com.frota_manager.inteligent_manager.dto.RegisterRequestDto;
import com.frota_manager.inteligent_manager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para autenticação e autorização
 * Gerencia login, registro e validação de tokens JWT
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * Endpoint para login de usuários
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest) {
        try {
            AuthResponseDto response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new AuthResponseDto(null, null, "Erro de autenticação: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para registro de novos usuários
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        try {
            AuthResponseDto response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new AuthResponseDto(null, null, "Erro no registro: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para validar token JWT
     */
    @PostMapping("/validate")
    public ResponseEntity<AuthResponseDto> validateToken(@RequestHeader("Authorization") String token) {
        try {
            AuthResponseDto response = authService.validateToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new AuthResponseDto(null, null, "Token inválido: " + e.getMessage())
            );
        }
    }
    
    /**
     * Endpoint para logout (invalidação de token)
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            authService.logout(token);
            return ResponseEntity.ok("Logout realizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no logout: " + e.getMessage());
        }
    }
    
    /**
     * Endpoint para verificar se o usuário está autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            AuthResponseDto response = authService.getCurrentUser(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                new AuthResponseDto(null, null, "Usuário não autenticado: " + e.getMessage())
            );
        }
    }
} 