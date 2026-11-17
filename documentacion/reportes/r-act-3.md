# Reporte de Avance del Proyecto

**Equipo:** 9  
**Fecha:** 16/nov/2026  
**Número de reporte:** 3  
**Repositorio GitHub:** https://github.com/ICRURO/desarrollo-web.git

---

## 1. Actividades realizadas

Se continuó el backend de GameBuildArchive con Java, Spring Boot y PostgreSQL. Los controladores GET y POST de videojuegos y publicaciones se ejecutaron mediante curl, comprobando respuestas HTTP 200 y 201 y la recuperación posterior de registros, ademas de la implementación los metodos PUT y DELETE.

Se desarrollaron filtros opcionales en GET /api/publicaciones por título (titulo), videojuego (videojuegoId) y autor (usuarioId). 

**Listado de actividades**

- Implementar PUT y DELETE.
- Incorporar filtros.
- Validar respuestas HTTP.
- Probar persistencia.

---

## 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
|---|---|---|
| Validar respuestas HTTP | Antonio de Jesus Tejeda López | |
| Incorporar filtros | Ian Carlo Rumayor Romero | |
| Implementar PUT y DELETE | José Fernando Martinez Enriquez | |
| Probar persistencia | José Fernando Martinez Enriquez | |

---

## 3. Problemas encontrados

Por errores de configuración de psql en wsl se hizo uso de consultas de IA para resolver el por qué springboot no se ejecutaba correctamente.

---

## 4. Avance funcional

Se tiene ya hecho la estructura del backend Spring Boot, la configuración de PostgreSQL, las entidades y repositorios para videojuegos y publicaciones.

Se verificó la compilación del proyecto mediante el comando:

```bash
./mvnw clean compile
```
---

## 5. Uso de IA generativa

- **Herramienta utilizada:** ChatGPT.
- **Actividad para la que se utilizó:** Implementación de JPA.
- **Código o contenido generado:** Solución de compilación de springboot con relacion a psql, consultas CURL.
- **Modificaciones realizadas por el equipo:** Adaptar la configuración a la que ya se tiene.
- **Forma en que se verificó el resultado:** Compilación del backend exitosa, consultas exitosas.

---

## 6. Participación individual

- **Integrante José Fernando Martinez Enriquez:** Implementar PUT y DELETE y probar persistencia.
- **Integrante Ian Carlo Rumayor Romero:**  Incorporar filtros
- **Integrante Antonio de Jesús Tejeda López:** Validar respuestas HTTP

---

## 7. Próximas actividades

- Crear formularios.
- Construir tablas.
- Implementar funciones Fetch.
- Mostrar errores y confirmaciones.
- Integrar frontend y backend.

---

## Observaciones adicionales

Aun se espera seguir evolucionando las funciones del backend a la par que se empieza el desarrollo del frontend.
