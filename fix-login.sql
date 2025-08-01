-- Script para inserir usuários de teste
-- Senha: admin123 (BCrypt hash)

-- Limpar usuários existentes
DELETE FROM users WHERE email IN ('admin@alten.com', 'manager@alten.com', 'driver@alten.com');

-- Inserir usuários de teste
INSERT INTO users (name, email, password, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) 
VALUES 
('Admin', 'admin@alten.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', true, true, true, true, NOW(), NOW()),
('Manager', 'manager@alten.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'MANAGER', true, true, true, true, NOW(), NOW()),
('Driver', 'driver@alten.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'DRIVER', true, true, true, true, NOW(), NOW());

-- Verificar usuários criados
SELECT email, name, role, enabled FROM users; 