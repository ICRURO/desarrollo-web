# GameBuildArchive | Comunidad de guías de videojuegos

## Descripción

GameBuildArchive es una aplicación web para consultar y compartir guías sobre videojuegos. Inicialmente contempla **Hearts of Iron IV**, **Zenless Zone Zero** y **Dying Light**.

La prioridad del proyecto es la comunidad de publicaciones públicas: usuarios identificados, guías y bloques de texto e imagen. Los comentarios y el simulador de configuraciones (Build Planner) son ampliaciones previstas para fases posteriores.

> **Estado — Entrega 3 (16 de noviembre de 2026):** se comprobaron GET y POST de videojuegos y publicaciones y filtros de publicaciones por título, videojuego y usuario, incluyendo combinaciones. PUT y DELETE fueron implementados y la validación integral de respuestas HTTP. 

## Tecnologías

- **Backend:** Java 17, Spring Boot 4.1.2, Maven, Spring Web MVC y Spring Data JPA (Hibernate).
- **Base de datos:** PostgreSQL (probado con PostgreSQL 18.6 en Ubuntu/WSL).
- **Frontend previsto:** HTML, CSS y JavaScript; API `fetch`.
- **Entorno:** Ubuntu en WSL sobre Windows
- **Control de versiones:** Git y GitHub.

## Estructura del repositorio

```text
desarrollo-web/
├── backend/                
│   ├── pom.xml
│   ├── mvnw
│   └── src/main/
│       ├── java/com/gamebuildarchive/backend/
│       └── resources/application.properties
├── database/
│   └── esquema.sql          
├── documentacion/
│   ├── diseño/              
│   ├── IAG/                
│   │   └── r-ia-dev-s03/
│   ├── reportes/
│   │   └── r-act-3.md
├── frontend/                
└── README.md
```

## Instalación en otra computadora (Windows con WSL)

Las instrucciones son para una instalación nueva. Si ya se cuenta con WSL, Git, Java o PostgreSQL, omitir esos pasos.

### 1. Preparar Ubuntu en WSL

Desde PowerShell en Windows, si WSL todavía no está instalado:

```powershell
wsl --install
```

Completar instalación de Ubuntu y reiniciar Windows si hace falta. Continuar los pasos desde Ubuntu/WSL.

### 2. Instalar Git, Java y PostgreSQL

```bash
sudo apt update
sudo apt install git openjdk-17-jdk postgresql postgresql-contrib -y
java -version
sudo service postgresql start
```

> La versión de PostgreSQL disponible puede variar. El proyecto fue probado con PostgreSQL 18.6.

### 3. Clonar el repositorio

```bash
mkdir -p ~/proyecto-final
cd ~/proyecto-final
git clone https://github.com/ICRURO/desarrollo-web.git
cd desarrollo-web
```

### 4. Crear usuario y base de datos PostgreSQL

```bash
sudo -u postgres psql
```

Dentro de `psql`:

```sql
CREATE USER gamebuild_user;
CREATE DATABASE gamebuild OWNER gamebuild_user;
\password gamebuild_user
\q
```

El comando `\password` establece la contraseña de forma interactiva. No incluir contraseñas reales en Git. Si el usuario y la base ya existen, no volver a crearlos.

### 5. Restaurar las 13 tablas

Desde la **raíz del repositorio**:

```bash
psql -h localhost -U gamebuild_user -d gamebuild -v ON_ERROR_STOP=1 -f database/esquema.sql
```

Importar en una base nueva y vacía. No repetir el script sobre tablas ya existentes.

Comprobar el esquema:

```bash
psql -h localhost -U gamebuild_user -d gamebuild
```

```sql
\dt
SELECT COUNT(*) FROM information_schema.tables
 WHERE table_schema = 'public' AND table_type = 'BASE TABLE';
\q
```

Se esperan 13 tablas.

### 6. Configurar Spring Boot

En `backend/src/main/resources/application.properties` se configura:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamebuild
spring.datasource.username=gamebuild_user
spring.datasource.password=${GAMEBUILD_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

`ddl-auto=none` evita modificaciones automáticas del esquema por Hibernate. La contraseña se indica en la misma terminal WSL donde se arranque Spring Boot:

```bash
read -r -s -p 'Contraseña de PostgreSQL: ' GAMEBUILD_DB_PASSWORD
echo
export GAMEBUILD_DB_PASSWORD
```

La variable es temporal para esa terminal; no se sube a Git.

### 7. Ejecutar backend

```bash
cd backend
./mvnw clean compile
./mvnw spring-boot:run
```

Si `mvnw` no es ejecutable, primero `chmod +x mvnw`. Con el backend iniciado, la API queda disponible en `http://localhost:8080`.

## Modelo de datos

Las 13 tablas se dividen en tres fases:

| Fase | Tablas | Alcance |
|---|---|---|
| 1. Comunidad | `usuarios`, `videojuegos`, `publicaciones`, `bloques_publicacion` | Prioridad de desarrollo |
| 2. Interacción | `comentarios`, `etiquetas`, `publicacion_etiquetas`, `reacciones` | Ampliación |
| 3. Enciclopedia y builds | `elementos_jugables`, `habilidades`, `equipamiento`, `builds_guardadas`, `build_equipamiento` | Ampliación posterior |

Diseño de datos: `documentacion/diseño/modelo-de-datos.md`.

## Avance de la entrega 3

Reporte: [`documentacion/reportes/r-act-3.md`](documentacion/reportes/r-act-3.md). 

Las sesiones con IA se documentan en `documentacion/IAG/r-ia-dev-s03/`.

## Solución de problemas frecuentes

- **`Connection refused` a PostgreSQL:** iniciar servicio con `sudo service postgresql start`.
- **`password authentication failed`:** verificar usuario, contraseña y valor de `GAMEBUILD_DB_PASSWORD`.
- **`Could not resolve placeholder GAMEBUILD_DB_PASSWORD`:** definir la variable en la misma terminal antes de ejecutar el backend.
- **`relation ... does not exist`:** importar `database/esquema.sql` y revisar `\dt`.
- **`Address already in use: 8080`:** detener otro proceso que use el puerto.
- **`findAll(Specification)` no encontrado al compilar:** asegurar que `PublicacionRepository` extienda `JpaSpecificationExecutor<Publicacion>`.
- **HTTP 500 en POST de publicación:** revisar registros de Spring Boot, campos obligatorios (`usuarioId`) y claves foráneas a usuarios y videojuegos existentes.
- **HTTP 404 en `/api/usuarios`:** puede no haber un controlador para ese endpoint; no implica que la tabla `usuarios` no exista.

## Repositorio

https://github.com/ICRURO/desarrollo-web
