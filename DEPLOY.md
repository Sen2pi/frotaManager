# 🚀 Guia de Deploy - Frota Manager

## 📋 Pré-requisitos

- Docker Desktop instalado e rodando
- Docker Compose instalado
- Mínimo 4GB de RAM disponível
- Portas 80 e 8080 disponíveis

## 🚀 Deploy Rápido

### Opção 1: Script Automático
```bash
# Dar permissão de execução
chmod +x start.sh

# Executar script de inicialização
./start.sh
```

### Opção 2: Comandos Manuais
```bash
# Parar containers existentes
docker-compose down

# Build e start dos containers
docker-compose up --build -d

# Verificar status
docker-compose ps
```

## 🔧 Configurações

### Variáveis de Ambiente

O sistema usa as seguintes variáveis de ambiente (já configuradas no docker-compose.yml):

#### Backend
- `SPRING_PROFILES_ACTIVE=prod` - Profile ativo
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/fleetdb` - URL do banco
- `SPRING_DATASOURCE_USERNAME=fleetuser` - Usuário do banco
- `SPRING_DATASOURCE_PASSWORD=fleetpass` - Senha do banco
- `JWT_SECRET=altenFleetManagementSecretKey2024ForSecureTokenGeneration` - Chave JWT
- `JWT_EXPIRATION=86400000` - Expiração do token (24h)

#### PostgreSQL
- `POSTGRES_DB=fleetdb` - Nome do banco
- `POSTGRES_USER=fleetuser` - Usuário
- `POSTGRES_PASSWORD=fleetpass` - Senha

## 🌐 URLs de Acesso

- **Frontend**: http://localhost:80
- **Backend API**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **PostgreSQL**: localhost:5432 (fleetdb)

## 📊 Monitoramento

### Verificar Logs
```bash
# Logs do backend
docker-compose logs backend

# Logs do frontend
docker-compose logs frontend

# Logs do PostgreSQL
docker-compose logs postgres

# Logs de todos os serviços
docker-compose logs -f
```

### Health Checks
```bash
# Verificar saúde do backend
curl http://localhost:8080/actuator/health

# Verificar saúde do frontend
curl http://localhost:80/health
```

## 🔍 Troubleshooting

### Problema: Backend não inicia
```bash
# Verificar logs detalhados
docker-compose logs backend --tail=50

# Verificar se o PostgreSQL está pronto
docker-compose logs postgres
```

### Problema: Frontend não carrega
```bash
# Verificar logs do nginx
docker-compose logs frontend

# Verificar se o proxy está funcionando
curl http://localhost:80/api/actuator/health
```

### Problema: CORS errors
- Verificar se as configurações CORS no backend estão corretas
- Verificar se o nginx está fazendo proxy corretamente

### Problema: Banco de dados não conecta
```bash
# Verificar se o PostgreSQL está rodando
docker-compose ps postgres

# Verificar logs do PostgreSQL
docker-compose logs postgres

# Conectar diretamente no banco
docker-compose exec postgres psql -U fleetuser -d fleetdb
```

## 🛠️ Comandos Úteis

```bash
# Parar todos os serviços
docker-compose down

# Parar e remover volumes (limpa banco)
docker-compose down -v

# Rebuild específico
docker-compose build backend
docker-compose build frontend

# Executar comando em container específico
docker-compose exec backend sh
docker-compose exec frontend sh
docker-compose exec postgres psql -U fleetuser -d fleetdb

# Verificar uso de recursos
docker stats
```

## 📝 Estrutura do Projeto

```
frotaManager/
├── backend/                 # API Spring Boot
│   ├── src/main/java/      # Código Java
│   ├── src/main/resources/ # Configurações
│   └── Dockerfile          # Build do backend
├── frontend/               # Aplicação Angular
│   ├── src/app/           # Código TypeScript
│   ├── src/environments/  # Configurações de ambiente
│   └── Dockerfile         # Build do frontend
├── docker-compose.yml     # Orquestração dos containers
├── init-db.sql           # Script de inicialização do banco
└── start.sh              # Script de inicialização
```

## 🔒 Segurança

- As senhas estão hardcoded para desenvolvimento
- Para produção, use variáveis de ambiente ou secrets
- JWT secret deve ser alterado em produção
- Configure HTTPS para produção

## 📈 Performance

- Backend: 512MB RAM mínimo
- Frontend: 256MB RAM mínimo
- PostgreSQL: 512MB RAM mínimo
- Total: ~1.5GB RAM recomendado

## 🔄 Atualizações

Para atualizar o sistema:

```bash
# Parar serviços
docker-compose down

# Pull das últimas imagens
docker-compose pull

# Rebuild e start
docker-compose up --build -d
``` 