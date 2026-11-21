# Reporte de Avance del Proyecto

**Equipo:** 9  
**Fecha:** 20/nov/2026  
**Número de reporte:** 4  
**Repositorio GitHub:** https://github.com/ICRURO/desarrollo-web.git

---

## 1. Actividades realizadas

Se avanzó en el desarrollo e integración del frontend de GameBuildArchive, utilizando HTML, CSS y JavaScript para permitir que los usuarios interactúen con las funciones del backend implementado con Java y Spring Boot y conectado a PostgreSQL.

Se implementaron formularios y listados de publicaciones, así como peticiones fetch a la API REST. Se incorporaron mensajes de error y confirmación para las operaciones realizadas desde la interfaz.

**Listado de actividades**

- Desarrollar la estructura y presentación del frontend.
- Crear formularios para registrar y editar publicaciones.
- Mostrar listados de videojuegos, artículos y guías.
- Integrar las operaciones GET, POST, PUT y DELETE mediante fetch.
- Conectar los filtros de búsqueda con el frontend.
- Comprobar la comunicación entre frontend, backend y PostgreSQL.

---

## 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit |
|---|---|---|
| Desarrollo de la interfaz y formularios | Antonio de Jesus Tejeda López | |
| Integración con API REST mediante fetch| José Fernando Martinez Enriquez | |
| Pruebas de funcionamiento e integración | José Fernando Martinez Enriquez |
| Actualización del README | Ian Carlo Rumayor Romero | |

---

## 3. Problemas encontrados

Durante las pruebas en un entorno local con WSL se presentó un problema de conexión entre el navegador y la API, debido a que otro servicio de Windows utilizaba el mismo puerto que el backend.

---

## 4. Avance funcional

Se cuenta con un frontend integrado con los servicios del backend y la base de datos PostgreSQL. Se confirmó con las siguientes pruebas desde la interfaz:
- Consulta de publicaciones almacenadas en PostgreSQL.
- Creación, edición y eliminación de publicaciones.
- Aplicación de filtros de búsqueda.
- Creación y eliminación de observaciones.
- Comunicación entre frontend y backend sin errores en las pruebas finales.  
Se verificó la respuesta de la API REST mediante curl, obteniendo HTTP 200 y datos en formato JSON para GET /api/publicaciones.
---

## 5. Uso de IA generativa

Para esta entrega no se reporta uso de IA Generativa

---

## 6. Participación individual

- **Integrante José Fernando Martinez Enriquez:** Desarrollo de interfaz, integración de funcionalidades y pruebas de integración.
- **Integrante Ian Carlo Rumayor Romero:**  Pruebas de integración, documentación.
- **Integrante Antonio de Jesús Tejeda López:** Desarrollo de la interfaz.

---

## 7. Próximas actividades

- Probar casos de exito y error
- Corregir defectos
- Completar README
- Preparar evidencias
- Verificar ejecucion en WSL

---

## Observaciones adicionales

Las funciones principales del sistema se encuentran integradas y probadas, las proximas mejoras antes de la ultima entrega serán visibles en ramas alternas.