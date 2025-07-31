## Projeto Sugerido: Sistema de Gestão de Frota Inteligente

### **Descrição do Projeto**

Desenvolva uma aplicação web para gestão de frotas de veículos com monitoramento em tempo real, ideal para o setor **Automotive** onde a ALTEN tem forte presença[^1].

### **Arquitetura Técnica**

#### **Backend (Java + Spring Boot)**

- **Spring Boot 3.x** com Java 17+
- **Spring Security** para autenticação JWT
- **Spring Data JPA** com base de dados H2/PostgreSQL
- **Spring Web** para APIs RESTful
- **WebSocket** para comunicação em tempo real
- **Spring Validation** para validação de dados


#### **Frontend (Angular)**

- **Angular 16+** com TypeScript
- **Angular Material** para componentes UI
- **RxJS** para programação reativa
- **Chart.js/D3.js** para dashboards e visualizações
- **Google Maps API** para tracking de localização


### **Funcionalidades Principais**

#### **Módulo de Veículos**

- CRUD completo de veículos (marca, modelo, matrícula, estado)
- Histórico de manutenções e inspeções
- Alertas automáticos para manutenção preventiva


#### **Módulo de Condutores**

- Gestão de condutores e licenças
- Associação veículo-condutor
- Relatórios de desempenho de condução


#### **Dashboard Analytics**

- Métricas de consumo de combustível
- Análise de rotas mais eficientes
- Indicadores KPI (quilómetros percorridos, custos operacionais)
- Gráficos interativos com dados em tempo real


#### **Sistema de Notificações**

- Alertas em tempo real via WebSocket
- Notificações push para manutenções
- Relatórios automáticos por email


### **Estrutura do Projeto**

```
fleet-management/
├── backend/
│   ├── src/main/java/com/fleetmanagement/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── config/
│   │   └── security/
│   └── pom.xml
└── frontend/
    ├── src/app/
    │   ├── components/
    │   ├── services/
    │   ├── models/
    │   ├── guards/
    │   └── modules/
    └── package.json
```


### **Tecnologias Adicionais para Destacar**

#### **DevOps \& Testing**

- **JUnit 5** e **Mockito** para testes unitários
- **Angular Testing Library** para testes frontend
- **Docker** para containerização
- **CI/CD** com GitHub Actions


#### **Monitorização**

- **Spring Actuator** para health checks
- **Micrometer** para métricas
- **Logging** estruturado com Logback





