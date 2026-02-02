# Loan API (Spring Boot Edition)

API REST para gestión de préstamos bancarios, desarrollada con Java y Spring Boot de acuerdo a los requerimientos técnicos.

## Requisitos Previos

*   **Java 17** (o Java 11 mínimo).
*   **Maven** (incluido mediante wrapper `mvnw` que se generaría al crear el proyecto, si no, tener maven instalado).

## Tecnologías

*   **Java**: Lenguaje principal.
*   **Spring Boot**: Framework.
*   **Spring Data JPA**: Abstracción para acceso a datos.
*   **H2 Database**: Base de datos en memoria (SQL).
*   **Lombok**: Reducción de código repetitivo (Getters/Setters).

## Estructura del Proyecto

*   `src/main/java/com/caixa/loanapi`: Código fuente.
    *   `controller`: Endpoints REST (`LoanController`).
    *   `service`: Lógica de negocio y validación de estados (`LoanService`).
    *   `repository`: Acceso a datos JPA (`LoanRepository`).
    *   `entity`: Modelo de datos (`Loan`).
    *   `dto`: Data Transfer Objects (`LoanRequestDto`, `LoanStatusDto`).

## Cómo Ejecutar

Desde la terminal en la raíz del proyecto:

```bash
# Si tienes maven instalado
mvn spring-boot:run
```

La aplicación iniciará en `http://localhost:8080`.

## Pruebas

Puedes probar la API usando el archivo `requests.http` incluido (requiere extensión REST Client en VS Code) o con cURL/Postman.

### Flujo de Estados

El servicio valida las siguientes transiciones:
*   PENDING -> APPROVED
*   PENDING -> REJECTED
*   APPROVED -> CANCELLED
*   Cualquier otra transición lanzará un error 400 Bad Request.
