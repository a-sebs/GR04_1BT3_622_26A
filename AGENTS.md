# AGENTS.md - SkillSwap Development Guide

## Quick Start

**Build & Run**:
```bash
# Start PostgreSQL (required)
docker-compose up -d

# Build and run the application
./mvnw clean compile spring-boot:run
```

**Default Credentials** (auto-seeded by DataInitializer):
- User: `demoUser` | Password: `demoPass1` | Email: `demo@skillswap.com`

## Project Architecture

**Stack**: Spring Boot 4.0.5 (Java 21) + PostgreSQL + JSP (server-rendered views)

**Core Layers**:
- `com.skillswap.model`: JPA entities (Usuario, PerfilHabilidades, Match, Sesion, Calificacion, Notificacion)
- `com.skillswap.service`: Business logic (MatchService, UsuarioService, NotificacionService, etc.)
- `com.skillswap.controller`: MVC controllers (RegistroController, MatchController, SesionController, CalificacionController)
- `com.skillswap.repository`: Spring Data JPA repositories
- `com.skillswap.config`: DataInitializer (CommandLineRunner for seed data)

**Critical Domain Concept**: The **Matching Algorithm** in `AlgoritmoMatching` calculates compatibility scores between users based on:
- Habilidades que busca (skills user wants to learn) vs. habilidades que ofrece (skills other user offers)
- Bidirectional matching: user1 learns from user2 AND user2 learns from user1
- Floating-point puntaje (0.0-1.0+) per Match entity

## Key Development Patterns

### 1. Constructor Injection (NOT @Autowired Fields)
All services use constructor injection. Example from MatchService:
```java
@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final AlgoritmoMatching algoritmoMatching;
    
    public MatchService(MatchRepository matchRepository, AlgoritmoMatching algoritmoMatching) {
        this.matchRepository = matchRepository;
        this.algoritmoMatching = algoritmoMatching;
    }
}
```
**Never** use field injection with @Autowired in new code.

### 2. Model-Level Validation
Validation logic lives in model entities, not just controllers. Example:
```java
public void registrar() {
    validarUsuario(nombre);
    validarPassword(password);
    validarCorreo(correo);
    // ... then update fields
}
```
Always validate BEFORE state mutations. Controllers call these model methods.

### 3. Transactional Boundaries
- Read-only queries: `@Transactional(readOnly = true)`
- Write operations: `@Transactional` with flush/delete order mattering
- DataInitializer uses `@Transactional` and coordinates deletes before inserts

### 4. Repository Query Patterns
Filter using queries like:
- `findByUsuarioId(Long)` → Optional
- `findByUsuarioSolicitanteIdOrderByCompatibilidadDesc(Long)` → List
- Query names are **explicit and self-documenting**

## Workflow: Adding a New Feature

1. **Model** (domain entity in `model/`): Define JPA entity with validation methods
2. **Repository** (in `repository/`): Extend CrudRepository with custom queries if needed
3. **Service** (in `service/`): Implement business logic, use constructor injection
4. **Controller** (in `controller/`): Route requests, call service, pass data to JSP model
5. **View** (in `src/main/webapp/WEB-INF/jsp/`): JSP with JSTL tags for rendering

**Example**: PerfilHabilidades has methods `coincideConFiltro()` and `coincideConNombreUsuario()` used by MatchService for filtering.

## Testing

**Test Configuration**:
- When `spring.profiles.active=test`, uses `application-test.yaml`
- Tests run against **H2 in-memory database** (no PostgreSQL needed for tests)
- Use `@SpringBootTest` for integration tests, `@ExtendWith(MockitoExtension.class)` for unit tests

**Test Pattern**:
```java
@SpringBootTest
@ActiveProfiles("test")
class MyTest { ... }

// OR

@ExtendWith(MockitoExtension.class)
class MyMockTest {
    @Mock private MyRepository repo;
    // ...
}
```

## Database & Deployment

**Local Development**:
- PostgreSQL runs in Docker: `docker-compose up -d`
- DataInitializer is idempotent (checks if demo user exists, won't re-insert)
- Hibernate DDL mode: `update` (auto-creates/updates schema)

**Environment Variables** (for Docker/deployment):
```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://host:5432/skillswap_db
SPRING_DATASOURCE_USERNAME: postgres
SPRING_DATASOURCE_PASSWORD: postgres
SPRING_JPA_HIBERNATE_DDL_AUTO: update
```

**Packaging**:
- WAR file: `./mvnw package` → `target/skillswap-0.0.1-SNAPSHOT.war`
- Includes embedded Tomcat via spring-boot-starter-web

## Common Pitfalls

- **Don't** use field injection; use constructor injection
- **Don't** skip validation at the model level
- **Don't** forget to mark test classes with `@ActiveProfiles("test")`
- **Don't** run tests without Docker stopped (H2 is in-memory, but PostgreSQL conflicts)
- **Always** validate before mutating model state
- **Always** use @Transactional(readOnly=true) for queries
- **Always** order repository method names to document intent (e.g., `...OrderByCompatibilidadDesc`)

## File Structure

```
src/main/
  java/com/skillswap/
    SkillSwapApplication.java        # Entry point
    controller/                      # @Controller classes, routing
    model/                           # JPA entities + validation
    service/                         # Business logic (inject into controllers)
    repository/                      # Spring Data JPA interfaces
    config/DataInitializer.java      # Seed data (CommandLineRunner)
  resources/
    application.yaml                 # Server config, datasource, JPA settings
    static/css/skillswap.css         # Stylesheets
  webapp/WEB-INF/jsp/                # JSP views (rendered server-side)

src/test/
  java/com/skillswap/
    controller/                      # Controller tests
    model/                           # Model/domain tests
    service/                         # Service/business logic tests
  resources/
    application-test.yaml            # H2 in-memory DB config
```

## String Conventions

This is a **Spanish-language project**. Expect:
- Method names in Spanish: `coincideConFiltro()`, `validarUsuario()`, `agregar()`, `editar()`, `eliminar()`
- Model fields in Spanish: `habilidadesOfrece`, `habilidadesBusca`, `reputacion`, `estado`
- Database table names in Spanish: `usuarios`, `perfiles_habilidades`, `matches`, `sesiones`

When adding new features, maintain Spanish naming conventions unless explicitly told otherwise.

## Debugging Tips

- **Port conflict on 5432?** Run `sudo lsof -i :5432` (Linux/macOS) to find process
- **Tests failing?** Check `@ActiveProfiles("test")` is set and H2 driver is on classpath
- **Hibernate logging?** Set `show-sql: true` and `format_sql: true` in application.yaml (already enabled)
- **No matches generated?** AlgoritmoMatching returns empty if perfilHabilidadesRepository is null (check autowiring)

