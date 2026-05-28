# 🏥 Salutis - Backend Healthcare

Backend profissional de saúde desenvolvido com **Spring Boot 4.0.6** e **Java 21**, focado em segurança, escalabilidade e boas práticas.

## � Sobre o Projeto

**Salutis** é um sistema de backend para gerenciamento de clínica/hospital com:
- ✅ Autenticação JWT segura
- ✅ Validação de senha forte (OWASP)
- ✅ Autorização por roles
- ✅ Documentação OpenAPI/Swagger
- ✅ Estrutura profissional pronta para produção

---

## 🚀 Quick Start

### Pré-requisitos
- Java 21
- PostgreSQL 12+
- Maven 3.8.9+

### Setup Local

1. **Clone o repositório**
   ```bash
   git clone https://github.com/joaonascimentooo/Salutis.git
   cd Salutis/Salutis
   ```

2. **Configure o banco de dados local**
   ```bash
   # Certifique-se que PostgreSQL está rodando
   psql -U postgres -c "CREATE DATABASE salutis_dev;"
   ```

3. **Configure variáveis de ambiente** (⚠️ OBRIGATÓRIO)
   
   **Opção A: Copiar template e preencher valores**
   ```bash
   # Copiar arquivo de exemplo
   cp .env.example .env
   
   # Editar .env com seus valores reais
   # - DB_URL: sua string de conexão PostgreSQL
   # - DB_USER: seu usuário do PostgreSQL
   # - DB_PASS: sua senha do PostgreSQL
   # - JWT_SECRET: gerar chave com openssl rand -base64 32
   ```

   **Opção B: PowerShell (Windows - não persistente)**
   ```powershell
   $env:DB_URL = "jdbc:postgresql://localhost:5432/salutis_dev"
   $env:DB_USER = "postgres"
   $env:DB_PASS = "sua_senha_aqui"
   $env:JWT_SECRET = "sua_chave_jwt_minimo_32_caracteres_aqui"
   $env:SPRING_PROFILES_ACTIVE = "dev"
   ```

4. **Execute a aplicação**
   ```bash
   # Desenvolvimento (carrega application-dev.properties)
   mvn clean spring-boot:run
   
   # Ou especificar perfil explicitamente
   SPRING_PROFILES_ACTIVE=dev mvn clean spring-boot:run
   ```

5. **Acesse a documentação interativa**
   - 📊 Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - 📄 OpenAPI Docs: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🌍 Configuração por Ambiente

O projeto suporta múltiplos ambientes através de Spring Profiles. Cada ambiente possui sua própria configuração:

### Arquivo Base: `application.properties`
- Contém **variáveis de ambiente** com placeholders `${VAR_NAME:default}`
- **DEVE ser commitado** ao Git (sem credenciais)
- Exemplo: `spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/salutis_dev}`

### Perfil DEV: `application-dev.properties`
- Configuração para **desenvolvimento local**
- Valores hardcoded seguros (localhost, usuário padrão)
- **DEVE ser commitado** (sem senhas reais)
- Logging em DEBUG
- Swagger/OpenAPI **habilitado**
- DDL Auto = `update` (altera schema automaticamente)

```bash
# Ativar perfil dev
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

### Perfil PROD: `application-prod.properties`
- Configuração para **produção**
- Usa variáveis de ambiente: `${DB_URL}`, `${JWT_SECRET}`, etc.
- **NÃO deve ser commitado** (protegido em .gitignore)
- Logging em WARN (mínimo)
- Swagger/OpenAPI **desabilitado**
- DDL Auto = `validate` (apenas valida schema)

```bash
# Ativar perfil prod
SPRING_PROFILES_ACTIVE=prod mvn spring-boot:run
```

### Variáveis de Ambiente Obrigatórias

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `DB_URL` | URL de conexão PostgreSQL | `jdbc:postgresql://localhost:5432/salutis_dev` |
| `DB_USER` | Usuário PostgreSQL | `postgres` |
| `DB_PASS` | Senha PostgreSQL | `sua_senha_aqui` |
| `JWT_SECRET` | Chave para assinar tokens JWT (min 32 chars) | Gerar: `openssl rand -base64 32` |
| `SPRING_PROFILES_ACTIVE` | Ambiente ativo (dev/prod) | `dev` |

### Como Fornecer Variáveis de Ambiente

**Opção 1: Arquivo `.env` (Desenvolvimento)**
```bash
cp .env.example .env
# Editar .env com valores reais
# .env é automaticamente ignorado no .gitignore
```

**Opção 2: Variáveis de Sistema (Produção)**
```bash
# Linux/Mac
export DB_URL="jdbc:postgresql://prod-server:5432/salutis"
export DB_USER="prod_user"
export DB_PASS="prod_password"
export JWT_SECRET="sua_chave_super_segura_aqui"
mvn spring-boot:run
```

**Opção 3: Docker/Kubernetes**
```yaml
env:
  - name: DB_URL
    valueFrom:
      secretKeyRef:
        name: salutis-secrets
        key: db-url
```

---

## 🔐 Segurança

Este projeto implementa boas práticas de segurança:

- ✅ **Proteção de Credenciais**: Variáveis de ambiente para dados sensíveis
- ✅ **JWT Seguro**: Tokens com expiração configurável
- ✅ **Validação de Senha**: Requisitos OWASP (8+ chars, maiúsculas, números, especiais)
- ✅ **Spring Security**: Autorização por roles (USER, ADMIN)
- ✅ **CORS Configurado**: Apenas origens autorizadas
- ✅ **Logging Seguro**: Informações sensíveis não são logadas

**⚠️ IMPORTANTE**: Veja [ENV_SETUP.md](./ENV_SETUP.md) para instruções completas de configuração segura.

---

## 🛠 Stack Tecnológico

| Tecnologia | Versão | Propósito |
|-----------|--------|----------|
| Java | 21 | Linguagem |
| Spring Boot | 4.0.6 | Framework |
| Spring Security | - | Autenticação |
| PostgreSQL | 12+ | Banco de Dados |
| JWT (jjwt) | 0.12.5 | Tokens |
| Lombok | - | Reduz Boilerplate |
| Swagger/OpenAPI | 2.0.4 | Documentação |
| Maven | 3.8.9+ | Build Tool |

---


## 🧪 Testes

### Executar testes
```bash
mvn test
```

### Testar endpoints com Swagger
1. Acesse [Swagger UI](http://localhost:8080/swagger-ui.html)
2. Registre um usuário em `POST /api/auth/register`
3. Faça login em `POST /api/auth/login`
4. Copie o token JWT
5. Clique em "Authorize" e cole o token
6. Agora você pode testar endpoints protegidos

---

## 🔧 Build & Deploy

### Build JAR para produção
```bash
mvn clean package -DskipTests
```

### Executar com Docker
```bash
docker build -t salutis:latest .
docker run -e DB_USERNAME=user -e DB_PASSWORD=pass -e JWT_SECRET=secret salutis:latest
```

---

## 📞 Endpoints Principais

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/auth/register` | ❌ Aberto | Registrar novo usuário |
| POST | `/api/auth/login` | ❌ Aberto | Login e obter JWT |
| GET | `/api/users/me` | ✅ JWT | Obter dados do usuário |
| GET | `/api/users` | ✅ JWT (ADMIN) | Listar todos os usuários |
| PUT | `/api/users/{id}` | ✅ JWT | Atualizar usuário |
| DELETE | `/api/users/{id}` | ✅ JWT (ADMIN) | Deletar usuário |

---

## �️ Roadmap do Projeto

Este projeto será evoluído progressivamente com:

**Fase 1: Qualidade** ⏳
- Testes automatizados (JUnit 5 + Mockito)
- Testes de integração com TestContainers
- Cobertura de código (JaCoCo)

**Fase 2: DevOps** 🚀
- Dockerfile para containerização
- Docker Compose para ambiente local
- CI/CD com GitHub Actions
- Automação de builds e testes

**Fase 3: Observabilidade** 📊
- Logging centralizado (ELK Stack)
- Monitoramento (Prometheus + Grafana)
- Rastreamento distribuído (OpenTelemetry)
- Health checks e métricas

**Fase 4: Funcionalidades de Negócio** 💼
- Gerenciamento de pacientes
- Agendamento de consultas
- Prescrições médicas
- Relatórios e dashboards
- Integração com sistemas externos

---

## 🤝 Contribuindo

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. Commit suas mudanças (`git commit -m 'Add some amazing feature'`)
4. Push para a branch (`git push origin feature/amazing-feature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](./LICENSE) para detalhes.

---

## 👤 Autor

**Jonas Nascimento**
- GitHub: [@joaonascimentooo](https://github.com/joaonascimentooo)
- Email: joaonascimentooo@example.com

---

## 🆘 Suporte

Encontrou um problema? 
- 📖 Verifique a [documentação](./ENV_SETUP.md)
- 🐛 Abra uma [issue](https://github.com/joaonascimentooo/Salutis/issues)
- 💬 Solicite um [pull request](https://github.com/joaonascimentooo/Salutis/pulls)

---

**Made with ❤️ by Jonas Nascimento**
