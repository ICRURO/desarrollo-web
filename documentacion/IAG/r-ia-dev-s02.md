# Reporte de desarrollo con IA — r-ia-dev-s02

**Proyecto:** GameBuildArchive  
**Integrante responsable:** Antonio de Jesús Tejeda López  
**Entrega:** 2 — Backend básico / API funcional inicial  
**Sesión:** 02 — Decisión de utilizar Spring Data JPA  
**Fecha de la sesión:** 12/11/2026  
**Herramienta:** ChatGPT (OpenAI)  
**Modelo:** GPT-6 (temperatura no disponible en la interfaz)  
**Código de uso:** Explicación / Diseño / Generación / Modificación / Pruebas  
**Repositorio:** https://github.com/ICRURO/desarrollo-web

## 1. Motivo de la consulta

El backend ya se había generado con Spring Boot, Maven, Java 17 y Spring Web MVC. La siguiente tarea de la segunda entrega era conectarlo con PostgreSQL y posteriormente crear entidades, repositorios y endpoints GET/POST. Antes de programar esas capas, se necesitaba decidir cómo acceder a los datos sin repetir instrucciones SQL y lógica de persistencia en cada controlador.

## 2. Prompt reconstruido

> Estoy desarrollando el backend de GameBuildArchive con Spring Boot 4.1.2 y Java 17. Utilizaremos PostgreSQL y tendremos tablas para usuarios, videojuegos, publicaciones y bloques de contenido. En clase trabajamos con clases como `Saludador`, `ISaludador` y `SaludarControlador`, pero no recuerdo haber utilizado JPA directamente. ¿Conviene agregar Spring Data JPA y `CrudRepository` en lugar de escribir todas las consultas SQL manualmente? Explícame por qué lo utilizaríamos, qué código nos ahorraría, si todavía necesitamos controladores REST y qué dependencias debo agregar al proyecto para conectarlo con PostgreSQL.

## 3. Respuesta de la IA

Sí, **te recomiendo utilizar Spring Data JPA** para GameBuildArchive, especialmente porque el segundo entregable les pide crear entidades, repositorios y una API con GET y POST. Además, en tus prácticas ya trabajaste con varios de sus conceptos, aunque quizá no los identificabas como JPA.

### ¿Qué es JPA y por qué lo utilizaríamos?

**JPA (Jakarta Persistence)** es una especificación que permite relacionar objetos Java con tablas de una base de datos relacional. Por ejemplo, la clase `Videojuego` puede representar la tabla `videojuegos`. Con anotaciones como `@Entity`, `@Table` e `@Id`, indicamos cómo se corresponden la clase y sus columnas.

**Hibernate** es una implementación de esa tecnología. **Spring Data JPA** facilita el acceso a los registros mediante interfaces como `CrudRepository`. Es una forma más sencilla de desarrollar las operaciones habituales de persistencia sin escribir manualmente un método SQL para cada inserción o consulta básica.

En vez de construir por nuestra cuenta consultas para guardar y buscar todos los videojuegos, podríamos definir:

```java
public interface IVideojuego extends CrudRepository<Videojuego, Long> {
}
```

A partir de esa interfaz tendremos operaciones comunes como `save()`, `findAll()`, `findById()` y `deleteById()`. **No significa que JPA haga todo automáticamente**: seguimos definiendo entidades, relaciones, validaciones y la lógica propia del proyecto.

### ¿JPA reemplaza los controladores?

**No.** Seguiremos necesitando controladores REST para recibir solicitudes HTTP y devolver respuestas. Por ejemplo, `VideojuegoControlador` atenderá `GET /api/videojuegos` y utilizará el repositorio para consultar registros. Lo que se simplifica es el **código de acceso a datos**, no la existencia del controlador.

La organización quedaría de esta manera:

1. **Entidad:** representa una tabla, por ejemplo `Videojuego.java`.
2. **Repositorio:** contiene las operaciones de persistencia, por ejemplo `IVideojuego.java`.
3. **Controlador REST:** recibe GET/POST y utiliza el repositorio, por ejemplo `VideojuegoControlador.java`.
4. **PostgreSQL:** almacena los datos de manera persistente.

Esta estructura se parece a la que ya conoces de `Saludador.java`, `ISaludador.java` y `SaludarControlador.java`.

### ¿Qué agregamos al proyecto?

Conserva la dependencia de Spring Web MVC y agrega estas dependencias en `<dependencies>` de `backend/pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Spring Data JPA** nos permitirá crear entidades y repositorios. El **driver PostgreSQL** permitirá que Java se comunique con el servidor de base de datos mediante JDBC.

Después, en `backend/src/main/resources/application.properties`, configura:

```properties
spring.application.name=backend

spring.datasource.url=jdbc:postgresql://localhost:5432/gamebuild
spring.datasource.username=gamebuild_user
spring.datasource.password=${GAMEBUILD_DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

Usamos `${GAMEBUILD_DB_PASSWORD}` para no escribir la contraseña real en un archivo que subiremos a GitHub. `ddl-auto=none` evita que Hibernate cree o modifique las tablas durante el arranque; para las tablas del modelo aprobado necesitaremos un script SQL o una estrategia de migración posterior.

Para probarlo desde WSL, inicia PostgreSQL, define la variable de entorno con tu contraseña en esa terminal y ejecuta:

```bash
sudo service postgresql start
read -r -s -p "Contraseña de PostgreSQL: " GAMEBUILD_DB_PASSWORD
echo
export GAMEBUILD_DB_PASSWORD
cd backend
./mvnw spring-boot:run
```

Si el backend inicia con los mensajes de HikariPool, el controlador JDBC, Hibernate/JPA y Tomcat, tendremos evidencia de que la **conexión** quedó configurada. Esto todavía no significa que las tablas o los endpoints GET/POST ya funcionen; esos serán los siguientes pasos.

**Recomendación:** utilicen Spring Data JPA y `CrudRepository` para reducir código repetitivo de persistencia y mantener los controladores enfocados en las peticiones HTTP. Implementen primero las entidades y repositorios de `usuarios`, `videojuegos`, `publicaciones` y `bloques_publicacion`, según el modelo aprobado por el equipo.

## 4. Código anterior y posterior

**Versión anterior (commit padre):** [`2defe13`](https://github.com/ICRURO/desarrollo-web/commit/2defe133b174ea67c4483c2667530f8819139be3)  
**Versión posterior:** [`b0d5833`](https://github.com/ICRURO/desarrollo-web/commit/b0d5833db17b8e26aac5502d084cd03573a5116c) — *Configurar conexion de Spring Boot con PostgreSQL*.

**Antes:** `backend/pom.xml` solo incluía Spring Web MVC y pruebas; `application.properties` solo contenía el nombre de la aplicación.

**Después:** se agregaron las dependencias de Spring Data JPA y PostgreSQL; se configuró la URL JDBC, usuario, contraseña mediante variable de entorno y ajustes de Hibernate.

**Archivos modificados:**

- `backend/pom.xml`
- `backend/src/main/resources/application.properties`

## 5. Diferencias entre versiones

Para obtener las diferencias desde el repositorio local:

```bash
git diff --numstat 2defe133b174ea67c4483c2667530f8819139be3 b0d5833db17b8e26aac5502d084cd03573a5116c
```

**Resultado verificado en el commit:**

```text
13      1       backend/pom.xml
9       0       backend/src/main/resources/application.properties
```

En total se añadieron 22 líneas y se eliminó 1. El cambio introduce la capa de acceso a datos y la conexión, sin implementar aún entidades ni controladores.

## 6. Prueba de funcionamiento

Se ejecutó el backend desde WSL y se revisó la salida de la terminal. En la captura compartida se observaron, entre otros, los siguientes mensajes:

```text
HikariPool-1 - Start completed.
Database JDBC URL [jdbc:postgresql://localhost:5432/gamebuild]
Database driver: PostgreSQL JDBC Driver
Database version: 18.6
Initialized JPA EntityManagerFactory
Tomcat started on port 8080
Started BackendApplication
```

**Resultado:** Spring Boot inició y se conectó a PostgreSQL. **Alcance de la prueba:** solo arranque y conexión; todavía no demuestra consultas a tablas ni operaciones GET/POST.

**Evidencia gráfica:** [Agregar aquí enlace relativo a la captura real guardada en el repositorio].

## 7. Reflexión del estudiante

Elegir JPA fue útil porque nos permite representar las tablas con clases Java y trabajar con repositorios sin escribir repetidamente las consultas básicas. Al revisar mis prácticas reconocí `@Entity` y `CrudRepository`, por lo que la solución no era completamente ajena a lo visto en clase. También comprendí que JPA **no elimina los controladores**: cada capa tiene una responsabilidad distinta. No aceptamos la configuración únicamente por la explicación de la IA, sino que verificamos el inicio de Spring Boot y la conexión con PostgreSQL desde WSL. Dejamos la implementación y prueba de entidades, repositorios y endpoints para los siguientes avances del equipo.

---

