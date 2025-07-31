# Configuração PostgreSQL - Instruções

## 1. Baixar e Instalar PostgreSQL
1. Acesse: https://www.postgresql.org/download/windows/
2. Baixe a versão mais recente (15 ou 16)
3. Execute o instalador

## 2. Configuração durante a instalação
- **Password do superuser (postgres):** `password`
- **Port:** `5432`
- **Locale:** Padrão
- **Componentes:** Manter todos selecionados (incluindo pgAdmin 4)

## 3. Após instalação
1. Abrir **pgAdmin 4** ou **SQL Shell (psql)**
2. Conectar com:
   - Host: `localhost`
   - Port: `5432`
   - User: `postgres`
   - Password: `password`

## 4. Criar a base de dados
Execute o comando:
```sql
CREATE DATABASE frota_manager;
```

## 5. Verificar serviço
O serviço PostgreSQL deve iniciar automaticamente. 
Nome do serviço: `postgresql-x64-15` (ou similar)

## 6. Testar aplicação
Após estes passos, execute a aplicação Spring Boot novamente.

## Troubleshooting
- Se o serviço não iniciar: Procure "Services" no Windows e inicie manualmente
- Se a conexão falhar: Verifique firewall e configurações de rede