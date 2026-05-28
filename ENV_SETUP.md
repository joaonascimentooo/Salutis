# 🔐 Configuração de Variáveis de Ambiente

Este projeto usa variáveis de ambiente para proteger informações sensíveis. **Nunca commitar credenciais em `application.properties`.**

## 📋 Variáveis Obrigatórias

```
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_postgres
JWT_SECRET=sua_chave_jwt_com_minimo_32_caracteres
```

## 🖥️ Setup Local (Windows - PowerShell)

### Opção 1: Definir direto no PowerShell
```powershell
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "sua_senha"
$env:JWT_SECRET = "sua_chave_secreta_minimo_32_chars"

# Depois rodar a aplicação
mvn spring-boot:run
```

### Opção 2: Usar arquivo `.env` (recomendado)

1. **Criar arquivo `.env` na raiz do projeto:**
```properties
DB_HOST=localhost
DB_PORT=5432
DB_NAME=salutis_db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_aqui
JWT_SECRET=sua_chave_jwt_minimo_32_caracteres
JWT_EXPIRATION=86400000
```

2. **Instalar plugin Maven** (adicionar ao `pom.xml`):
```xml
<plugin>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>maven-dotenv-plugin</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <goals>
                <goal>load</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

3. **Rodar projeto:**
```bash
mvn spring-boot:run
```

## 🐳 Setup Docker/Produção

As variáveis devem ser definidas no container ou plataforma:

**Docker:**
```bash
docker run -e DB_USERNAME=postgres -e DB_PASSWORD=senha -e JWT_SECRET=chave app:latest
```

**Docker Compose:**
```yaml
environment:
  - DB_USERNAME=${DB_USERNAME}
  - DB_PASSWORD=${DB_PASSWORD}
  - JWT_SECRET=${JWT_SECRET}
```

## ✅ Verificar Configuração

O Spring Boot vai falhar com erro se as variáveis obrigatórias não estiverem definidas.

```
Error: Required env variable 'DB_PASSWORD' is not set
```

## 🔒 Boas Práticas

✅ **FAZER:**
- Usar variáveis de ambiente
- Manter `.env` no `.gitignore`
- Usar secrets manager em produção (AWS Secrets, Azure Vault, etc)

❌ **NÃO FAZER:**
- Commitar credenciais
- Usar senhas padrão em produção
- Expor JWT secret no código
