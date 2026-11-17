# Reporte de desarrollo con IA — r-ia-dev-s02

**Proyecto:** GameBuildArchive  
**Integrante responsable:** Ian Carlo Rumayor Romero  
**Entrega:** 3 — API REST completa  
**Sesión:** 03 — Decisión de utilizar Spring Data JPA  
**Fecha de la sesión:** 16/11/2026  
**Herramienta:** ChatGPT (OpenAI)  
**Modelo:** GPT-6 (temperatura no disponible en la interfaz)  
**Código de uso:** Explicación / Diseño / Generación / Modificación / Pruebas  
**Repositorio:** https://github.com/ICRURO/desarrollo-web

## 1. Motivo de la consulta

Poder hacer consultas mediante CURL, resolver unos problemas con psql en wls y verificar que el proyecto tuviera las librerias correspondientes.

## 2. Prompt reconstruido

> Ayúdame a implementar y probar filtros de búsqueda para las publicaciones de GameBuildArchive, desarrollado con Java, Spring Boot y PostgreSQL. Necesito filtrar publicaciones por título, videojuego y usuario, además de combinar varios filtros en una misma consulta. Guíame para modificar las clases necesarias, resolver errores de compilación, configurar la conexión con PostgreSQL en WSL y verificar el funcionamiento de los endpoints mediante peticiones curl.

## 3. Respuesta de la IA

Vamos a implementar y probar los filtros de búsqueda de GameBuildArchive utilizando Java, Spring Boot y PostgreSQL. Trabajaremos paso a paso para que puedas entender cada modificación y comprobar su funcionamiento desde WSL.

### 1. ¿Qué vamos a implementar?

Actualmente, GameBuildArchive permite consultar publicaciones mediante peticiones GET. Vamos a ampliar esta funcionalidad para realizar búsquedas más específicas.

Filtro	Endpoint

Por título	/api/publicaciones?titulo=guia

Por videojuego	/api/publicaciones?videojuegoId=1

Por usuario	/api/publicaciones?usuarioId=1

Filtros combinados	/api/publicaciones?titulo=guia&videojuegoId=1&usuarioId=1

Para implementarlos utilizaremos JpaSpecificationExecutor y Specification, que permiten construir consultas dinámicas sin escribir una consulta SQL diferente para cada combinación.

### 2. Modificar PublicacionRepository.java

Abre el archivo:

`backend/src/main/java/com/gamebuildarchive/backend/repository/PublicacionRepository.java`

Agrega la interfaz JpaSpecificationExecutor conservando los métodos existentes.

```
package com.gamebuildarchive.backend.repository;

import com.gamebuildarchive.backend.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository
        extends JpaRepository<Publicacion, Long>,
                JpaSpecificationExecutor<Publicacion> {

    List<Publicacion> findByVideojuegoId(Long videojuegoId);

    List<Publicacion> findByTituloContainingIgnoreCase(String titulo);
}
```

¿Para qué sirve? JpaRepository proporciona las operaciones básicas sobre la base de datos, mientras que JpaSpecificationExecutor permite combinar filtros mediante condiciones dinámicas.

### 3. Implementar los filtros en PublicacionController.java

Dentro de PublicacionController.java, reemplaza únicamente el método GET que lista publicaciones por el siguiente:

```
@GetMapping
public ResponseEntity<List<Publicacion>> listarPublicaciones(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false) Long videojuegoId,
        @RequestParam(required = false) Long usuarioId) {

    List<Specification<Publicacion>> filtros = new ArrayList<>();

    // Buscar por titulo
    if (titulo != null && !titulo.isBlank()) {
        String busqueda = titulo.trim().toLowerCase();

        filtros.add((root, query, cb) ->
            cb.like(
                cb.lower(root.get("titulo")),
                "%" + busqueda + "%"
            )
        );
    }

    // Buscar por videojuego
    if (videojuegoId != null) {
        filtros.add((root, query, cb) ->
            cb.equal(
                root.get("videojuego").get("id"),
                videojuegoId
            )
        );
    }

    // Buscar por usuario
    if (usuarioId != null) {
        filtros.add((root, query, cb) ->
            cb.equal(root.get("usuarioId"), usuarioId)
        );
    }

    List<Publicacion> resultados;

    if (filtros.isEmpty()) {
        resultados = publicacionRepository.findAll();
    } else {
        resultados = publicacionRepository.findAll(
            Specification.allOf(filtros)
        );
    }

    return ResponseEntity.ok(resultados);
}
```

Asegúrate de tener estos imports:

```
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
```

Con esta implementación, los filtros se combinan mediante condiciones AND. Es decir, una publicación debe cumplir todas las condiciones proporcionadas.

### 4. Compilar y resolver posibles errores

Desde la terminal WSL, ejecuta:

```cd ~/proyectos/desarrollo-web/backend
./mvnw clean compile
```

El resultado esperado es:

`[INFO] BUILD SUCCESS`

Si aparece un error relacionado con:

`no suitable method found for findAll(Specification<Publicacion>)`

Revisa que PublicacionRepository extienda correctamente 

`JpaSpecificationExecutor<Publicacion>.`

Este error ocurre porque JpaRepository por sí solo no proporciona el método necesario para ejecutar consultas con Specification.

### 5. Configurar PostgreSQL en WSL

Tu proyecto utiliza la siguiente configuración en application.properties:

``` 
spring.datasource.url=jdbc:postgresql://localhost:5432/gamebuild
spring.datasource.username=gamebuild_user
spring.datasource.password=${GAMEBUILD_DB_PASSWORD}
```

Primero verifica que PostgreSQL esté funcionando:

```
sudo service postgresql status
```

Si todavía no tienes configurado el usuario y la base de datos en tu entorno local, puedes crearlos desde PostgreSQL:
```
sudo -u postgres psql
```

Dentro de la consola:
```
CREATE USER gamebuild_user WITH PASSWORD 'TU_CONTRASEÑA';
CREATE DATABASE gamebuild OWNER gamebuild_user;
```

Estos comandos solo deben ejecutarse si el usuario y la base de datos no existen.

Después importa el esquema SQL del repositorio, utilizando su ruta correspondiente:
```
psql -h localhost -U gamebuild_user -d gamebuild -f database/esquema.sql
```

Finalmente, configura la contraseña para Spring Boot:
```
export GAMEBUILD_DB_PASSWORD='TU_CONTRASEÑA'
```

La contraseña debe coincidir con la que asignaste al usuario de PostgreSQL.

### 6. Iniciar Spring Boot

Desde la misma terminal donde configuraste la variable de entorno:
```
./mvnw spring-boot:run
```

Si la conexión funciona correctamente, deberías observar mensajes similares a:
```
HikariPool-1 - Start completed.
Tomcat started on port 8080 (http)
Started BackendApplication
```

Esto indica que el backend está listo para recibir peticiones HTTP.

### 7. Probar los filtros mediante curl

Abre una segunda terminal WSL, manteniendo Spring Boot ejecutándose en la primera.

Vamos a utilizar los siguientes comandos:

Prueba 1. Consultar todas las publicaciones
```
curl -i "http://localhost:8080/api/publicaciones"
```

Prueba 2. Filtrar por título
```
curl -i "http://localhost:8080/api/publicaciones?titulo=guia"
```

Prueba 3. Filtrar por videojuego
```
curl -i "http://localhost:8080/api/publicaciones?videojuegoId=1"
```

Prueba 4. Filtrar por usuario
```
curl -i "http://localhost:8080/api/publicaciones?usuarioId=1"
```

Prueba 5. Combinar los tres filtros
```
curl -i "http://localhost:8080/api/publicaciones?titulo=guia&videojuegoId=1&usuarioId=1"
```

Los IDs deben corresponder a registros existentes en PostgreSQL.

## 4. Código anterior y posterior

**Versión anterior (commit padre):** [`2defe13`](https://github.com/ICRURO/desarrollo-web/commit/b0dc33455719e6a5a80015049c6558b6a15b148d)  
**Versión posterior:** [`b0d5833`](https://github.com/ICRURO/desarrollo-web/commit/b0d5833db17b8e26aac5502d084cd03573a5116c) 

**Archivos modificados:**

- `backend/src/main/java/com/gamebuildarchive/backend/Controller/PublicacionController.Java`
- `backend/src/main/java/com/gamebuildarchive/backend/repository/PublicacionRepository.Java`

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

Se ejecutó el backend desde WSL y se revisó la salida de la terminal. 

**Resultado:** Spring Boot inició y se conectó a PostgreSQL. **Alcance de la prueba:** Operaciones GET/POST.

## 7. Reflexión del estudiante

Se buscó no cometer errores antes de acabar con lo más importante de la parte del backend, las consultas de CURL funcionaron correctamente y se documentó el proceso de configuracion de psql en wsl para evitar cometer errores de conexión con la base de datos.

---
