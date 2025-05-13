# Prontuário Digital

A modern, secure, and efficient digital medical records system built with Spring Boot microservices architecture. Streamlining healthcare documentation while ensuring data privacy and accessibility.

Sistema de Prontuário Digital Hospitalar desenvolvido com Spring Boot 3.2+ e Java 21.

## 🚀 Arquitetura

O sistema é composto por 4 microsserviços:

1. **Authentication Service**
   - Autenticação e autorização (OAuth2 + JWT)
   - Gerenciamento de usuários e roles

2. **Patient Service**
   - Gestão de pacientes e leitos
   - Histórico médico
   - Status de internação

3. **Notification Service**
   - Notificações em tempo real
   - WebSocket para atualizações
   - Integração com Kafka

4. **Monitoring Service**
   - Métricas e monitoramento
   - Integração com Prometheus
   - Dashboards Grafana

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- Kafka
- Docker
- Kubernetes
- Prometheus
- Grafana

## 📋 Pré-requisitos

- JDK 21
- Maven 3.8+
- Docker e Docker Compose
- Git

## 🔧 Configuração do Ambiente

1. Clone o repositório:
```bash
git clone git@github.com:josivantarcio/ProntoarioDigital.git
cd ProntoarioDigital
```

2. Inicie os serviços com Docker Compose:
```bash
docker-compose up -d
```

3. Execute o build do projeto:
```bash
mvn clean install
```

4. Inicie os serviços:
```bash
# Authentication Service
mvn spring-boot:run -pl authentication-service

# Patient Service
mvn spring-boot:run -pl patient-service

# Notification Service
mvn spring-boot:run -pl notification-service

# Monitoring Service
mvn spring-boot:run -pl monitoring-service
```

## 📚 Documentação da API

A documentação Swagger está disponível em:
- Authentication Service: http://localhost:8081/swagger-ui.html
- Patient Service: http://localhost:8082/swagger-ui.html
- Notification Service: http://localhost:8083/swagger-ui.html
- Monitoring Service: http://localhost:8084/swagger-ui.html

## 🔐 Segurança

- Autenticação via JWT
- Roles: ROLE_DOCTOR, ROLE_NURSE, ROLE_ADMIN
- Criptografia AES-256 para dados sensíveis
- Rate limiting para APIs públicas

## 📊 Monitoramento

- Métricas disponíveis em: http://localhost:8084/actuator
- Grafana Dashboard: http://localhost:3000
- Prometheus: http://localhost:9090

## 🤝 Contribuição

1. Fork o projeto
2. Crie sua Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a Branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Autores

* **Josivan Tarcio** - *Desenvolvimento* - [josivantarcio](https://github.com/josivantarcio) 