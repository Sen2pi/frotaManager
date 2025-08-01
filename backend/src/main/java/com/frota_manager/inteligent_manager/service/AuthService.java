package com.frota_manager.inteligent_manager.service;

import com.frota_manager.inteligent_manager.dto.AuthRequestDto;
import com.frota_manager.inteligent_manager.dto.AuthResponseDto;
import com.frota_manager.inteligent_manager.dto.RegisterRequestDto;
import com.frota_manager.inteligent_manager.model.User;
import com.frota_manager.inteligent_manager.model.UserRole;
import com.frota_manager.inteligent_manager.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço de autenticação e autorização
 */
@Service
public class AuthService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    /**
     * Realiza login do usuário
     */
    public AuthResponseDto login(AuthRequestDto authRequest) {
        try {
            System.out.println("🔐 AuthService: Iniciando login para " + authRequest.email());
            
            // Verificar se o usuário existe
            User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + authRequest.email()));
            
            System.out.println("👤 Usuário encontrado: " + user.getEmail() + " (enabled: " + user.isEnabled() + ")");
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.email(),
                    authRequest.password()
                )
            );
            
            System.out.println("✅ Autenticação bem-sucedida para: " + authRequest.email());
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = generateToken(userDetails);
            
            // Atualiza último login
            user.setLastLogin(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            userRepository.save(user);
            
            System.out.println("🎫 Token gerado com sucesso para: " + authRequest.email());
            
            return new AuthResponseDto(token, userDetails.getUsername(), "Login realizado com sucesso");
        } catch (Exception e) {
            System.out.println("❌ Erro no login para " + authRequest.email() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Credenciais inválidas: " + e.getMessage());
        }
    }
    
    /**
     * Registra novo usuário
     */
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        // Valida se as senhas coincidem
        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new RuntimeException("As senhas não coincidem");
        }
        
        // Verifica se o email já existe
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        // Cria novo usuário
        User user = new User(
            registerRequest.name(),
            registerRequest.email(),
            passwordEncoder.encode(registerRequest.password()),
            UserRole.valueOf(registerRequest.role().toUpperCase())
        );
        
        userRepository.save(user);
        
        // Gera token para o novo usuário
        String token = generateToken(user);
        
        return new AuthResponseDto(token, user.getEmail(), "Usuário registrado com sucesso");
    }
    
    /**
     * Valida token JWT
     */
    public AuthResponseDto validateToken(String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(cleanToken)
                .getPayload();
            
            String email = claims.getSubject();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            return new AuthResponseDto(cleanToken, user.getEmail(), "Token válido");
        } catch (Exception e) {
            throw new RuntimeException("Token inválido");
        }
    }
    
    /**
     * Obtém usuário atual
     */
    public AuthResponseDto getCurrentUser(String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(cleanToken)
                .getPayload();
            
            String email = claims.getSubject();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            return new AuthResponseDto(cleanToken, user.getEmail(), "Usuário autenticado");
        } catch (Exception e) {
            throw new RuntimeException("Usuário não autenticado");
        }
    }
    
    /**
     * Realiza logout (invalidação de token)
     */
    public void logout(String token) {
        // Em uma implementação real, você pode adicionar o token a uma blacklist
        // Por simplicidade, apenas validamos se o token é válido
        validateToken(token);
    }
    
    /**
     * Gera token JWT
     */
    private String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key)
            .compact();
    }
    
    /**
     * UserDetailsService implementation
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
} 