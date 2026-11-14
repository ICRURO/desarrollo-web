# GameBuildArchive | Comunidad de guías de videojuegos

## Descripción

GameBuildArchive es una aplicación web para consultar y compartir guías sobre videojuegos. Inicialmente contempla **Hearts of Iron IV**, **Zenless Zone Zero** y **Dying Light**.

La prioridad del proyecto es la **comunidad de publicaciones públicas**: usuarios identificados, guías y bloques de texto e imagen. Los comentarios y el simulador de configuraciones (*Build Planner*) son ampliaciones previstas para fases posteriores.

> **Estado actual — Entrega 2:** el proyecto Spring Boot inicia y se ha comprobado su conexión con PostgreSQL. El esquema de las 13 tablas está disponible en `database/esquema.sql`. La integración de los controladores REST, las operaciones GET/POST y las pruebas con `curl` todavía están en desarrollo; no se presenta aquí como una API terminada.

## Tecnologías

- **Backend:** Java 17, Spring Boot 4.1.2, Maven, Spring Web MVC y Spring Data JPA (Hibernate).
- **Base de datos:** PostgreSQL (probado con PostgreSQL 18.6 en Ubuntu/WSL).
- **Frontend previsto:** HTML, CSS y JavaScript, mediante `fetch` para consumir la API REST.
- **Entorno utilizado:** Ubuntu en WSL sobre Windows.
- **Control de versiones:** Git y GitHub.

## Estructura del repositorio

```text
desarrollo-web/
├── backend/                       # Proyecto Maven / Spring Boot
│   ├── pom.xml
│   ├── mvnw
│   └── src/main/
│       ├── java/com/gamebuildarchive/backend/
│       └── resources/application.properties
├── database/
│   └── esquema.sql                 # Estructura de las 13 tablas
├── documentacion/
│   ├── diseño/                     # Modelo y documentos de diseño
│   ├── IAG/                        # Informes de uso de IA
│   └── reportes/                   # Reportes de avance
├── frontend/                       # Interfaz en desarrollo
└── README.md
```

## Instalación en otra computadora (Windows con WSL)

Estas instrucciones están pensadas para una instalación nueva. Si ya se cuenta con WSL, Git, Java o PostgreSQL, se puede omitir la instalación correspondiente.

### 1. Preparar Ubuntu en WSL

Desde PowerShell en Windows, si WSL todavía no está instalado:

```powershell
wsl --install
```

Completar la instalación de Ubuntu y reiniciar Windows si el sistema lo solicita. Abrir Ubuntu/WSL para continuar con los siguientes comandos.

### 2. Instalar Git, Java y PostgreSQL

Desde la terminal de Ubuntu/WSL:

```bash
sudo apt update
sudo apt install git openjdk-17-jdk postgresql postgresql-contrib -y
```

Comprobar Java e iniciar PostgreSQL:

```bash
java -version
sudo service postgresql start
```

> La versión de PostgreSQL disponible puede variar según la versión de Ubuntu. El proyecto se ha probado con PostgreSQL 18.6.

### 3. Clonar el repositorio

```bash
mkdir -p ~/proyecto-final
cd ~/proyecto-final
git clone https://github.com/ICRURO/desarrollo-web.git
cd desarrollo-web
```

### 4. Crear usuario y base de datos PostgreSQL

Ingresar como usuario administrador de PostgreSQL:

```bash
sudo -u postgres psql
```

Dentro de `psql`, ejecutar:

```sql
CREATE USER gamebuild_user;
CREATE DATABASE gamebuild OWNER gamebuild_user;
\password gamebuild_user
\q
```

El comando `\password` solicita una contraseña de forma interactiva. Conservarla: se necesitará para importar el esquema y ejecutar Spring Boot. **No agregar esta contraseña al repositorio.**

> Los comandos `CREATE USER` y `CREATE DATABASE` se usan en una instalación nueva. Si ya existen, no es necesario repetirlos.

### 5. Restaurar las 13 tablas

Desde la **carpeta raíz del repositorio** (`desarrollo-web/`), no desde `psql`, ejecutar:

```bash
psql -h localhost -U gamebuild_user -d gamebuild -v ON_ERROR_STOP=1 -f database/esquema.sql
```

Ingresar la contraseña definida en el paso anterior. Este script debe importarse en una base de datos nueva y vacía; no está diseñado para ejecutarse repetidamente sobre tablas existentes.

Comprobar las tablas:

```bash
psql -h localhost -U gamebuild_user -d gamebuild
```

Dentro de `psql`:

```sql
\dt
SELECT COUNT(*)
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type = 'BASE TABLE';
\q
```

El resultado esperado del conteo es **13**.

### 6. Configurar la contraseña para Spring Boot

La conexión ya está definida en `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gamebuild
spring.datasource.username=gamebuild_user
spring.datasource.password=${GAMEBUILD_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

La propiedad `spring.jpa.hibernate.ddl-auto=none` evita que Hibernate genere o modifique automáticamente el esquema: las tablas se crean con el SQL del paso 5.

Para indicar la contraseña de manera temporal en la terminal WSL:

```bash
read -r -s -p "Contraseña de PostgreSQL: " GAMEBUILD_DB_PASSWORD
echo
export GAMEBUILD_DB_PASSWORD
```

La variable solo se conserva en esa sesión de terminal. Si se abre otra terminal o se reinicia WSL, habrá que definirla nuevamente para arrancar el backend.

### 7. Ejecutar el backend

En **la misma terminal** donde se definió `GAMEBUILD_DB_PASSWORD`:

```bash
cd backend
./mvnw spring-boot:run
```

Si el script no tiene permiso de ejecución, usar primero `chmod +x mvnw` dentro de `backend/`.

En un inicio correcto se observan mensajes similares a estos:

```text
HikariPool-1 - Start completed.
Initialized JPA EntityManagerFactory
Tomcat started on port 8080
Started BackendApplication
```

El servidor queda escuchando en **http://localhost:8080**. Una respuesta HTTP 404 en `/` puede ser normal mientras no exista un controlador que atienda esa ruta. Para detenerlo, presionar `Ctrl + C`.

## Modelo de datos y estado de desarrollo

Las 13 tablas del modelo se organizan en tres fases:

| Fase | Tablas | Alcance |
|---|---|---|
| 1. Comunidad | `usuarios`, `videojuegos`, `publicaciones`, `bloques_publicacion` | Prioridad del desarrollo |
| 2. Interacción | `comentarios`, `etiquetas`, `publicacion_etiquetas`, `reacciones` | Ampliación |
| 3. Enciclopedia y builds | `elementos_jugables`, `habilidades`, `equipamiento`, `builds_guardadas`, `build_equipamiento` | Ampliación posterior |

El documento de diseño está en `documentacion/diseño/modelo-de-datos.md`.

**Avances comprobados hasta ahora:** proyecto Spring Boot creado; dependencia de Spring Data JPA y driver PostgreSQL agregadas; conexión a PostgreSQL comprobada desde WSL; creación y exportación de 13 tablas; entidades y repositorios iniciales de `Videojuego` y `Publicacion`; corrección de la entidad `Publicacion` para adaptarla al esquema; compilación de Maven satisfactoria.

**Pendiente de integrar y verificar:** entidad de usuarios y bloques, correspondencia de todas las entidades con el esquema, controladores y endpoints GET/POST, inserción de datos de prueba y pruebas con `curl`. El sistema de inicio de sesión tampoco está implementado todavía.

## Solución de problemas frecuentes

- **`Connection refused` al conectar con PostgreSQL:** comprobar que se inició con `sudo service postgresql start`.
- **`password authentication failed`:** revisar el usuario y su contraseña; el valor de `GAMEBUILD_DB_PASSWORD` debe coincidir con la contraseña asignada a `gamebuild_user`.
- **`Could not resolve placeholder GAMEBUILD_DB_PASSWORD`:** definir y exportar la variable en la misma terminal antes de iniciar Spring Boot.
- **`relation ... does not exist`:** importar `database/esquema.sql` en la base `gamebuild` y verificar `\dt`.
- **`Address already in use: 8080`:** comprobar si otro proceso ya está utilizando ese puerto y detenerlo antes de volver a arrancar.

## Repositorio

https://github.com/ICRURO/desarrollo-web
