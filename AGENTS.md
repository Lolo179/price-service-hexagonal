# AGENTS

Notas para agentes de IA o revisores automatizados que trabajen con este repositorio.

## Estructura del proyecto

Arquitectura hexagonal (ports & adapters):

```
price-service/
├── contract/
│   └── swagger-contract.yaml        # Fuente de verdad de la API (contract-first)
├── src/
│   ├── main/java/com/inditex/prices/
│   │   ├── domain/
│   │   │   ├── model/               # Price.java, Currency.java
│   │   │   └── exception/           # PriceNotFoundException.java
│   │   ├── application/
│   │   │   ├── port/
│   │   │   │   ├── in/              # GetApplicablePriceUseCase.java
│   │   │   │   └── out/             # PriceRepositoryPort.java
│   │   │   └── service/             # GetApplicablePriceService.java
│   │   ├── infrastructure/
│   │   │   ├── input/rest/          # PriceController.java
│   │   │   │   ├── api/             # PricesApi.java (generado)
│   │   │   │   ├── dto/             # PriceResponse.java, ErrorResponse.java, Currency.java (generados)
│   │   │   │   └── mapper/          # PriceRestMapper.java
│   │   │   └── output/persistence/  # PriceEntity.java, PriceJpaRepository.java, PricePersistenceAdapter.java
│   │   │       └── mapper/          # PricePersistenceMapper.java
│   │   ├── shared/exception/        # GlobalExceptionHandler.java, ErrorMessages.java
│   │   ├── config/                  # OpenApiConfig.java
│   │   └── PriceServiceApplication.java
│   ├── main/resources/
│   │   ├── application.yml          # Configuración base (todos los entornos)
│   │   ├── application-local.yml    # Overrides locales — NO commiteado (en .gitignore)
│   │   └── data.sql                 # Datos de prueba (4 filas)
│   └── test/
│       ├── java/.../
│       │   ├── application/service/
│       │   │   └── GetApplicablePriceServiceTest.java   # Unit Mockito (capa aplicación)
│       │   ├── infrastructure/output/persistence/
│       │   │   └── PricePersistenceAdapterTest.java     # @DataJpaTest (capa persistencia)
│       │   ├── infrastructure/input/rest/
│       │   │   └── PriceE2ETest.java                    # RestAssured spring-mock-mvc E2E
│       │   └── PriceServiceApplicationTests.java        # Context load test
│       └── resources/
│           ├── application-test.properties
│           └── price-service.postman_collection.json
├── .github/
│   └── workflows/
│       └── ci.yml               # GitHub Actions CI (triggered on PR to main)
├── Dockerfile                       # Multi-stage build (Maven builder + JRE 21 runtime)
├── pom.xml
└── README.md
```

## Clases generadas (no editar manualmente)

Las siguientes clases se generan automáticamente en cada `mvn compile` a partir de `contract/swagger-contract.yaml` y están excluidas de git:

- `src/main/java/com/inditex/prices/infrastructure/input/rest/api/PricesApi.java`
- `src/main/java/com/inditex/prices/infrastructure/input/rest/dto/PriceResponse.java`
- `src/main/java/com/inditex/prices/infrastructure/input/rest/dto/ErrorResponse.java`
- `src/main/java/com/inditex/prices/infrastructure/input/rest/dto/Currency.java`

Para modificar la API, editar **únicamente** `contract/swagger-contract.yaml`.

## Comandos útiles

```bash
# Compilar (también regenera las clases del contrato)
.\mvnw.cmd compile

# Ejecutar todos los tests
.\mvnw.cmd test

# Arrancar la aplicación (perfil por defecto — sin H2 Console, logs INFO)
.\mvnw.cmd spring-boot:run

# Arrancar con perfil local (H2 Console en /h2-console, logs DEBUG, show-sql)
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
# o con variable de entorno:
$env:SPRING_PROFILES_ACTIVE="local"; .\mvnw.cmd spring-boot:run

# Construir el JAR
.\mvnw.cmd package -DskipTests

# Construir imagen Docker
docker build -t price-service .

# Ejecutar contenedor
docker run -d -p 8080:8080 --name price-service price-service

# Comprobar health (requiere aplicación arrancada)
curl http://localhost:8080/actuator/health
```

## Convenciones

- Spring Boot 4.0.5 / Java 21 / Spring Framework 7.x
- Arquitectura hexagonal: `domain` → `application` → `infrastructure` + `shared`
- Los paquetes de test slice (`@DataJpaTest`, `@AutoConfigureMockMvc`) están en sus propios starters Boot 4.x: `spring-boot-starter-data-jpa-test` y `spring-boot-starter-webmvc-test`
- `Currency` existe como enum en dos sitios: `domain/model/Currency.java` (dominio) y el generado `infrastructure/input/rest/dto/Currency.java` (DTO). MapStruct los mapea automáticamente.
- Los mensajes de error están centralizados en `shared/exception/ErrorMessages.java`
- `PriceController` implementa `PricesApi` (generado) e inyecta `GetApplicablePriceUseCase` y `PriceRestMapper`
- `PricePersistenceAdapter` implementa `PriceRepositoryPort` e inyecta `PriceJpaRepository` y `PricePersistenceMapper`
- Tests E2E usan `io.rest-assured:spring-mock-mvc:6.0.0` (requiere 6.x para compatibilidad con Spring Framework 7)
- La colección Postman en `src/test/resources/price-service.postman_collection.json` cubre los 5 casos del ejercicio más 4 casos de error
- `spring-boot-starter-actuator` expone únicamente `/actuator/health` (`show-details: never`). El CI usa este endpoint para la comprobación de arranque en lugar del endpoint de negocio

## Perfiles de configuración

| Perfil | Archivo | Commiteado | Descripción |
|--------|---------|------------|-------------|
| (ninguno) | `application.yml` | ✅ Sí | Base — seguro para todos los entornos |
| `local` | `application-local.yml` | ❌ No | H2 Console, show-sql, logs DEBUG |
| `test` | `application-test.yml` | ✅ Sí | H2 aislado (`testdb`), logs WARN |

`application-local.yml` está en `.gitignore`. Si no existe, la aplicación arranca igualmente con los valores de `application.yml`.
