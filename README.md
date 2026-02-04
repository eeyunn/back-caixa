# 🏦 Loan API - Sistema de Gestión de Préstamos

API REST para la gestión de solicitudes de préstamos bancarios, desarrollada con **Java 21** y **Spring Boot 3.1.3**.

## 📋 Descripción

Esta aplicación permite crear y gestionar solicitudes de préstamos, controlando sus estados y transiciones según reglas de negocio definidas. Utiliza una base de datos H2 en memoria para persistencia de datos.

## ✅ Requisitos Previos

- **Java 21** (JDK 21)
- **Maven 3.6+** (o usar el wrapper incluido `mvnw`)

## 🛠️ Tecnologías

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 3.1.3 | Framework principal |
| Spring Data JPA | 3.1.3 | Capa de persistencia |
| H2 Database | Runtime | Base de datos en memoria |
| Lombok | 1.18.34 | Reducción de código boilerplate |
| Spring Validation | 3.1.3 | Validación de datos |

## 📁 Estructura del Proyecto

```
src/main/java/com/caixa/loanapi/
├── controller/          # Endpoints REST
│   └── LoanController.java
├── service/            # Lógica de negocio
│   └── LoanService.java
├── repository/         # Acceso a datos
│   └── LoanRepository.java
├── entity/             # Entidades JPA
│   ├── Loan.java
│   └── LoanStatus.java (enum)
├── dto/                # Data Transfer Objects
│   ├── LoanRequestDto.java
│   └── LoanStatusDto.java
└── LoanApiApplication.java
```

## 🚀 Cómo Ejecutar

### Opción 1: Con Maven instalado
```bash
mvn spring-boot:run
```

### Opción 2: Con Maven Wrapper
```bash
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

La aplicación iniciará en **`http://localhost:8080`**

## 📡 Endpoints Disponibles

### 1️⃣ Crear Solicitud de Préstamo
```http
POST http://localhost:8080/loans
Content-Type: application/json

{
  "applicantName": "Juan Pérez",
  "amount": 50000.00,
  "currency": "EUR",
  "documentId": "12345678Z"
}
```

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "applicantName": "Juan Pérez",
  "amount": 50000.00,
  "currency": "EUR",
  "documentId": "12345678Z",
  "status": "PENDING",
  "createdAt": "2024-01-15T10:30:00"
}
```

### 2️⃣ Listar Todas las Solicitudes
```http
GET http://localhost:8080/loans
```

### 3️⃣ Obtener Solicitud por ID
```http
GET http://localhost:8080/loans/1
```

### 4️⃣ Actualizar Estado de Solicitud
```http
PATCH http://localhost:8080/loans/1/status
Content-Type: application/json

{
  "status": "APPROVED"
}
```

## 🔄 Estados y Transiciones

### Estados Disponibles
- **PENDING**: Solicitud creada, pendiente de revisión
- **APPROVED**: Solicitud aprobada
- **REJECTED**: Solicitud rechazada
- **CANCELLED**: Solicitud cancelada

### Transiciones Válidas
```
PENDING ─────┬───> APPROVED ───> CANCELLED
             │
             └───> REJECTED
```

| Desde | Hacia | ✅ Válido |
|-------|-------|-----------|
| PENDING | APPROVED | ✅ |
| PENDING | REJECTED | ✅ |
| APPROVED | CANCELLED | ✅ |
| APPROVED | REJECTED | ❌ |
| REJECTED | * | ❌ |
| CANCELLED | * | ❌ |

**Nota:** Cualquier transición no válida retornará `400 Bad Request` con mensaje de error.

## 🧪 Probar la API

### Usando el archivo `requests.http`
Si usas **VS Code** con la extensión [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client), puedes ejecutar directamente las peticiones del archivo `requests.http` incluido.

### Usando cURL

**Crear préstamo:**
```bash
curl -X POST http://localhost:8080/loans \
  -H "Content-Type: application/json" \
  -d '{
    "applicantName": "María García",
    "amount": 30000.00,
    "currency": "EUR",
    "documentId": "87654321A"
  }'
```

**Listar préstamos:**
```bash
curl http://localhost:8080/loans
```

**Aprobar préstamo:**
```bash
curl -X PATCH http://localhost:8080/loans/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "APPROVED"}'
```

## 🗄️ Base de Datos H2

La aplicación usa H2 en memoria. Para acceder a la consola H2:

1. Accede a: `http://localhost:8080/h2-console`
2. Configura:
   - **JDBC URL**: `jdbc:h2:mem:loandb`
   - **User**: `sa`
   - **Password**: *(vacío)*

## 📝 Validaciones

El sistema valida:
- ✅ Campos obligatorios en las solicitudes
- ✅ Formato de montos (positivos)
- ✅ Transiciones de estado válidas
- ✅ Existencia de préstamos antes de actualizar

## 🐛 Manejo de Errores

| Código | Escenario |
|--------|-----------|
| 400 | Transición de estado inválida |
| 404 | Préstamo no encontrado |
| 500 | Error interno del servidor |

## 🏗️ Arquitectura y Decisiones Técnicas

### Arquitectura por Capas

El proyecto sigue una **arquitectura en capas** (Layered Architecture) para mantener separación de responsabilidades:

```
┌─────────────────────────────────────┐
│      Controller Layer               │  ← Endpoints REST (API)
├─────────────────────────────────────┤
│      Service Layer                  │  ← Lógica de negocio
├─────────────────────────────────────┤
│      Repository Layer               │  ← Acceso a datos (JPA)
├─────────────────────────────────────┤
│      Database (H2)                  │  ← Persistencia en memoria
└─────────────────────────────────────┘
```

### Decisiones Técnicas

#### 1. **Java 21 + Spring Boot 3.1.3**
- **Por qué:** Versiones modernas con mejoras de rendimiento y seguridad
- **Beneficio:** Acceso a pattern matching, records, y mejoras del compilador
- **Compatible con:** JDK 17+ (requisito mínimo del enunciado)

#### 2. **H2 Database en memoria**
- **Por qué:** El enunciado permite almacenamiento en memoria, pero opté por H2 para mayor profesionalismo
- **Beneficio:** Persistencia real con SQL, fácil migración a bases de datos productivas (PostgreSQL, MySQL)
- **Consola H2:** Permite inspeccionar datos durante desarrollo

#### 3. **Spring Data JPA**
- **Por qué:** Abstracción de alto nivel para acceso a datos
- **Beneficio:** Reduce boilerplate, permite cambiar de BD fácilmente
- **Repository pattern:** `LoanRepository` con métodos CRUD automáticos

#### 4. **DTOs (Data Transfer Objects)**
- **Por qué:** Separar la capa de presentación de las entidades JPA
- **Beneficio:** Evita exponer estructura interna, permite validaciones específicas
- **Implementación:** `LoanRequestDto` y `LoanStatusDto`

#### 5. **Lombok**
- **Por qué:** Reducir código repetitivo (getters, setters, constructores)
- **Beneficio:** Código más limpio y mantenible
- **Versión 1.18.34:** Compatible con JDK 21

#### 6. **Validación de Estados con Máquina de Estados**
- **Por qué:** El enunciado requiere validar transiciones específicas
- **Implementación:** Método `validateStateTransition()` en `LoanService.java:49`
- **Estados terminales:** REJECTED y CANCELLED no permiten más transiciones

#### 7. **RESTful API Design**
- **POST /loans:** Creación (201 Created)
- **GET /loans:** Listado completo
- **GET /loans/{id}:** Recurso específico (404 si no existe)
- **PATCH /loans/{id}/status:** Modificación parcial (idempotente)

#### 8. **Manejo de Errores Centralizado**
- **@ExceptionHandler:** Maneja `IllegalArgumentException` globalmente
- **ResponseEntity:** Devuelve códigos HTTP apropiados (400, 404, 500)
- **Mensajes descriptivos:** Ayudan al cliente a entender el error

### Patrones de Diseño Utilizados

- **Repository Pattern:** Abstracción de acceso a datos
- **DTO Pattern:** Separación entre entidades y contratos de API
- **Dependency Injection:** Spring gestiona dependencias automáticamente
- **State Machine:** Validación de transiciones de estados

## 🚀 Mejoras Futuras

### Mejoras Funcionales

#### 1. **Autenticación y Autorización**
- Implementar Spring Security con JWT
- Roles: `CLIENTE` (solo lectura de sus préstamos), `GESTOR` (modificar estados)
- Endpoints protegidos según roles

#### 2. **Auditoría Completa**
- Registrar quién modificó cada estado y cuándo
- Tabla `loan_audit_log` con historial de cambios
- Implementar `@CreatedBy`, `@LastModifiedBy` de Spring Data JPA

#### 3. **Notificaciones**
- Enviar emails cuando cambia el estado (aprobado/rechazado)
- Integración con servicios de mensajería (RabbitMQ, Kafka)
- Notificaciones push para apps móviles

#### 4. **Validaciones de Negocio Avanzadas**
- Límite de monto según tipo de cliente
- Verificación de scoring crediticio
- Validación de documentos contra API externa
- Límite de préstamos activos por cliente

#### 5. **Búsqueda y Filtrado**
- Filtrar por estado: `GET /loans?status=PENDING`
- Filtrar por rango de fechas: `GET /loans?from=2024-01-01&to=2024-12-31`
- Búsqueda por nombre o documento
- Paginación para grandes volúmenes: `GET /loans?page=0&size=20`

#### 6. **Dashboard de Métricas**
- Total de préstamos por estado
- Monto total aprobado/rechazado
- Tiempo promedio de aprobación
- Tasa de aprobación

#### 7. **Documentación Interactiva**
- Integrar Swagger/OpenAPI 3.0
- Endpoint `/swagger-ui.html` con documentación completa
- Ejemplos de peticiones y respuestas

### Mejoras Técnicas/Arquitecturales

#### 1. **Testing Completo**
```java
// Tests unitarios
@Test void shouldCreateLoanWithPendingStatus()
@Test void shouldValidateStateTransitions()

// Tests de integración
@SpringBootTest
@AutoConfigureMockMvc
class LoanControllerIntegrationTest

// Tests de contrato con Pact
```

#### 2. **Migración a Arquitectura Hexagonal**
```
domain/          ← Lógica de negocio pura
  ├── model/     ← Entidades de dominio
  ├── ports/     ← Interfaces (in/out)
  └── service/   ← Casos de uso
infrastructure/  ← Adaptadores
  ├── rest/      ← Controllers
  └── jpa/       ← Repositories
```

#### 3. **Event-Driven Architecture**
- Publicar eventos de dominio: `LoanCreatedEvent`, `LoanApprovedEvent`
- Otros microservicios pueden suscribirse (ej: servicio de notificaciones)
- Implementar con Spring Cloud Stream

#### 4. **Caché con Redis**
- Cachear listado de préstamos: `@Cacheable("loans")`
- Invalidar caché al crear/modificar
- Mejorar rendimiento en consultas frecuentes

#### 5. **Base de Datos Productiva**
- Migrar a PostgreSQL o MySQL
- Flyway/Liquibase para migraciones versionadas
- Índices en campos frecuentemente consultados

#### 6. **Observabilidad**
```yaml
# Métricas: Prometheus + Grafana
# Trazas: Spring Cloud Sleuth + Zipkin
# Logs: ELK Stack (Elasticsearch, Logstash, Kibana)
```

#### 7. **API Gateway y Microservicios**
- Separar en microservicios:
  - `loan-service`: Gestión de préstamos
  - `customer-service`: Gestión de clientes
  - `notification-service`: Notificaciones
- API Gateway con Spring Cloud Gateway
- Service Discovery con Eureka

#### 8. **CI/CD Pipeline**
```yaml
# GitHub Actions / GitLab CI
- Build y test automáticos
- Análisis de código (SonarQube)
- Deploy automático a staging/producción
- Docker + Kubernetes para orquestación
```

#### 9. **Rate Limiting y Throttling**
- Limitar peticiones por cliente (100 req/min)
- Protección contra ataques DDoS
- Implementar con Bucket4j o Redis

#### 10. **API Versioning**
```
/api/v1/loans    ← Versión actual
/api/v2/loans    ← Futuras mejoras
```

## 📄 Licencia

Este proyecto es parte de una prueba técnica para CaixaBank.
