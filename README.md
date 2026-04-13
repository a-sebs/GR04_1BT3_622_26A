# SkillSwap - Plataforma de Intercambio de Habilidades

## 📋 Descripción del Proyecto

**SkillSwap** es una plataforma web moderna diseñada para facilitar el intercambio y la enseñanza mutua de habilidades entre usuarios. Permite que personas con diferentes conocimientos se conecten, compartan conocimientos y realicen sesiones de tutoría o colaboración. 

El proyecto utiliza un algoritmo de **matching inteligente** que sugiere usuarios compatibles en función de habilidades ofrecidas y buscadas, creando así un ecosistema dinámico de aprendizaje colaborativo.

### 🎯 Objetivos Principales
- Conectar usuarios con intereses y habilidades complementarias
- Facilitar la gestión de sesiones de intercambio de conocimiento
- Proporcionar un sistema de calificación y reputación
- Implementar un algoritmo de matching basado en compatibilidad de habilidades
- Ofrecer una interfaz intuitiva y amigable para la navegación

---

## 🛠️ Stack Tecnológico

### Backend
- **Java 21**: Lenguaje de programación principal
- **Spring Boot 4.0.5**: Framework para desarrollo de aplicaciones web
- **Spring Data JPA**: Acceso a datos con Hibernate ORM
- **Spring Validation**: Validación de datos con Jakarta Validation
- **Tomcat Embebido**: Servidor web integrado

### Frontend
- **JSP (JavaServer Pages)**: Vistas dinámicas del lado del servidor
- **JSTL (Jakarta Standard Tag Library)**: Etiquetas estándar para JSP
- **CSS3**: Estilos y diseño responsivo
- **HTML5**: Estructura semántica

### Base de Datos
- **PostgreSQL 18**: Sistema de gestión de base de datos relacional
- **Docker & Docker Compose**: Contenerización de la base de datos

### Dependencias Clave
- **Lombok**: Reducción de código boilerplate
- **PostgreSQL Driver (postgresql-42.7.10)**: Conector JDBC para PostgreSQL
- **Jakarta API**: Framework estándar de Java EE

---

## 📦 Características

✅ **Autenticación y Registro de Usuarios**
- Sistema de login seguro con validación de credenciales
- Registro de nuevos usuarios con validación de datos
- Gestión de perfiles personalizados

✅ **Algoritmo de Matching Inteligente**
- Análisis de compatibilidad entre usuarios basado en habilidades
- Sugerencias personalizadas de matches
- Cálculo de puntuaciones de compatibilidad

✅ **Gestión de Habilidades**
- Perfiles de habilidades (habilidades que ofrece/busca)
- Sistema de reputación y calificaciones
- Catálogo de habilidades disponibles

✅ **Sesiones y Calendario**
- Planificación de sesiones de intercambio
- Sistema de disponibilidad
- Confirmación de solicitudes de contacto

✅ **Interfaz Responsiva**
- Diseño moderno con navegación intuitiva
- Compatibilidad multiplataforma
- Experiencia de usuario optimizada

---

## 🚀 Guía de Instalación y Configuración

### Requisitos Previos
- **Java 21** o superior
- **Maven 3.8+** (se incluye con Maven wrapper)
- **Docker & Docker Compose**
- **Git** (opcional)

### ⚠️ Importante: Evitar Conflictos de Puertos

Si tienes **PostgreSQL instalado localmente**, debes **desactivar el servicio** antes de ejecutar Docker para evitar conflictos en el puerto `5432`:

#### En Windows (PowerShell - Administrador):
```powershell
# Detener el servicio de PostgreSQL local
Stop-Service -Name postgresql-x64-18 -Force

# Verificar que el puerto 5432 esté disponible
netstat -ano | findstr :5432
```

#### En Linux:
```bash
# Detener el servicio de PostgreSQL local
sudo systemctl stop postgresql

# Verificar que el puerto 5432 esté disponible
sudo lsof -i :5432
```

#### En macOS:
```bash
# Detener PostgreSQL con Homebrew
brew services stop postgresql

# Verificar que el puerto 5432 esté disponible
lsof -i :5432
```

### 📝 Pasos de Instalación

#### 1. Clonar el Repositorio
```bash
git clone <URL_DEL_REPOSITORIO>
cd GR04_1BT3_622_26A
```

#### 2. Iniciar Docker Compose (Base de Datos)

Asegúrate de que PostgreSQL local no esté corriendo, luego:

```bash
# Iniciar los contenedores en segundo plano
docker-compose up -d

# Verificar que el contenedor esté corriendo
docker ps

# Ver logs del contenedor de base de datos
docker-compose logs -f database
```

**Detalles del Contenedor:**
- Nombre del contenedor: `skillswap_db_container`
- Base de datos: `skillswap_db`
- Usuario: `postgres`
- Contraseña: `postgres`
- Puerto: `5432`
- Volumen persistente: `postgres_data`

#### 3. Compilar el Proyecto

```bash
# Limpiar compilaciones anteriores
./mvnw clean

# Compilar el proyecto
./mvnw compile

# Compilar y empaquetar (generar WAR)
./mvnw package
```

#### 4. Ejecutar la Aplicación

```bash
# Ejecutar la aplicación Spring Boot
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## 🗄️ Configuración de la Base de Datos

### Archivo de Configuración: `application.yaml`

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/skillswap_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Inicialización de Datos (DataInitializer)

Al iniciar la aplicación por primera vez, se ejecuta automáticamente el `DataInitializer.java`, que carga datos demo:

- **Usuario Demo**: 
  - Usuario: `demoUser`
  - Contraseña: `demoPass1`
  - Correo: `demo@skillswap.com`

- **Usuarios Adicionales**: Ana, Luis y María con diferentes habilidades y compatibilidades

Esta inicialización es **idempotente**, por lo que no genera errores si se ejecuta múltiples veces.

---

## 🐳 Comandos Docker Útiles

### Gesionar el Contenedor

```bash
# Iniciar los contenedores
docker-compose up -d

# Detener los contenedores
docker-compose down

# Detener contenedores pero mantener volúmenes (datos)
docker-compose down --volumes

# Ver estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs solo del servicio de base de datos
docker-compose logs -f database

# Reiniciar el contenedor
docker-compose restart

# Acceder a la terminal de PostgreSQL
docker exec -it skillswap_db_container psql -U postgres -d skillswap_db
```

### Comandos PostgreSQL Útiles (Desde la Terminal del Contenedor)

```sql
-- Ver todas las bases de datos
\l

-- Conectarse a la base de datos de SkillSwap
\c skillswap_db

-- Listar todas las tablas
\dt

-- Ver estructura de una tabla
\d usuarios

-- Ver todos los usuarios
SELECT * FROM usuarios;

-- Limpiar una tabla
TRUNCATE TABLE usuarios CASCADE;
```

---

## 📁 Estructura del Proyecto

```
GR04_1BT3_622_26A/
├── src/
│   ├── main/
│   │   ├── java/com/skillswap/
│   │   │   ├── controller/          # Controladores MVC
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Interfaces de acceso a datos
│   │   │   ├── service/             # Lógica de negocio
│   │   │   ├── config/              # Configuración (DataInitializer)
│   │   │   └── SkillSwapApplication.java
│   │   ├── resources/
│   │   │   ├── application.yaml     # Configuración de Spring
│   │   │   └── static/css/skillswap.css
│   │   └── webapp/WEB-INF/jsp/      # Vistas JSP
│   └── test/
├── docker-compose.yml               # Configuración de Docker
├── pom.xml                          # Dependencias Maven
└── README.md                        # Este archivo
```

---

## 🔧 Desarrollo Local

### Configurar IDE (IntelliJ IDEA / Eclipse)

1. Abre el proyecto como Maven project
2. Espera a que Maven descargue todas las dependencias
3. Configura el SDK de Java a la versión 21
4. Ejecuta la aplicación desde `SkillSwapApplication.java`

### Estructura de Modelos

- **Usuario**: Información de usuarios (nombre, correo, contraseña)
- **PerfilHabilidades**: Habilidades que ofrece/busca cada usuario
- **Match**: Compatibilidad y matches generados
- **Calificación**: Sistema de puntuación entre usuarios
- **Sesión**: Registro de sesiones de intercambio


## ⚠️ Solución de Problemas Comunes

### Error: "La autenticación password falló para el usuario 'postgres'"
**Causa**: PostgreSQL local está corriendo en el puerto 5432
**Solución**: Detén el servicio de PostgreSQL local (ver sección "Evitar Conflictos de Puertos")

### Error: "El puerto 5432 ya está en uso"
```bash
# Encontrar qué proceso usa el puerto
lsof -i :5432  # macOS/Linux
netstat -ano | findstr :5432  # Windows
```

### La aplicación no inicia
1. Verifica que Docker esté corriendo
2. Verifica que los contenedores de Docker estén activos: `docker-compose ps`
3. Revisa los logs: `docker-compose logs`
4. Asegúrate de tener Java 21: `java -version`

### Datos no aparecen en la aplicación
1. Verifica que el contenedor de PostgreSQL está corriendo
2. Verifica la conectividad a la BD (ver sección "Comandos PostgreSQL Útiles")
3. Revisa los logs de Spring Boot para errores de conexión

---

## 📝 Licencia

Este proyecto es parte del curso de **Metodologías Ágiles**.

**Grupo**: GR04_1BT3_622_26A

---

## 👥 Contribuidores

Proyecto desarrollado en equipo como parte de la asignatura **Metodologías Ágiles**.

---

**Última actualización**: 12 de Abril de 2026
