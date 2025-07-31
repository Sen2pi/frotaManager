package com.frota_manager.inteligent_manager.repository;

import com.frota_manager.inteligent_manager.model.User;
import com.frota_manager.inteligent_manager.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de usuário
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários por role
     */
    List<User> findByRole(UserRole role);
} 