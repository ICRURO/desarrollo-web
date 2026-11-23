# Reporte de Avance del Proyecto

**Equipo:** 9
**Fecha:** 22/nov/2026
**Número de reporte:** 5
**Repositorio GitHub:** https://github.com/ICRURO/desarrollo-web.git


---

## 1. Actividades realizadas

Durante esta quinta etapa enfocada en **Pruebas, Documentación y Versión Candidata**, se realizaron las siguientes actividades:

**Listado de actividades**

- Ejecución de pruebas integrales de flujo completo verificando casos de éxito y manejo de errores HTTP.
-Corrección de defectos detectados en la comunicación entre el frontend y los controladores REST del backend.
- Verificación de la clonación, instalación y ejecución completa del sistema desde cero en un entorno limpio de WSL
- Actualización del archivo `README.md` con las instrucciones detalladas de despliegue y reproducción en WSL.  
- Actualizacion de la documentación.

## 2. Evidencias de trabajo

| Actividad | Integrante responsable | Enlace a commit | 
| ----- | ----- | ----- | 
| Pruebas de endpoints y validación de persistencia en PSQL | Antonio de Jesus Tejeda Lopez |  | 
| Corrección de errores en controladores REST y limpieza de configuración CORS | Ian Carlo Rumayor Romero|  | 
| Validación de formularios y manejo de alertas/errores en Frontend | José Fernando Martinez Enriquez |  | 
| Documentación | Ian Carlo Rumayor Romero |  | 

## 3. Problemas encontrados

* **Problema Configuración del sitio para abrir en navegador:** Exisitia dificultad para que el navegador ingresara al sitio en uno de los 3 dispositivos del equipo, no validaba la comunicación entre los distintos puertos que conformaban el sitio.
El problema se resolvió con ajustes de permisos y configuraciones en el proyecto.

## 4. Avance funcional

 **Funcionalidades:**

  - Registro y autenticación de usuarios (`UsuarioController`, `login.html`, `registro.html`).
  - Catálogo y filtrado por videojuegos (`VideojuegoController`).
  - Gestión completa (CRUD) de publicaciones/builds (`PublicacionController`, `index.html`, `app.js`).
  - Persistencia completa en MySQL mediante Spring Data JPA.


## 5. Uso de IA generativa

 **Herramienta utilizada:** ChatGPT (GPT-4o) / Claude

 **Actividad para la que se utilizó:** Creación e integración de casos de prueba (peticiones CURL y pruebas de integración) y generación de ilustracion para el fondo del sitio.

 **Código o contenido generado:** Comandos CURL para probar respuestas de error (400 Bad Request, 404 Not Found) en los controladores de Publicaciones y Comentarios, imagen que combina las tres imagenes dadas.

 **Modificaciones realizadas por el equipo:** Se adaptaron las rutas de los endpoints, los puertos locales y los payloads JSON a los atributos reales de nuestras entidades (`Usuario`, `Publicacion`, `Comentario`, `Videojuego`).

 **Forma en que se verificó el resultado:** Ejecutando cada comando desde la terminal de WSL contra el servidor Spring Boot activo y verificando los códigos de estado HTTP devueltos y los registros insertados en MySQL.

## 6. Participación individual

* **José Fernando Martinez Enriquez:** Validacion de formulario y verificación de alertas.

* **Ian Carlo Rumayor Romero:** Corrección de errores, documentación.

* **Antonio de Jesus Tejeda Lipez:** Pruebas de Endpoints, validacion de persistencia PSQL, correción de errores.

## 7. Próximas actividades

- Publicación del video demostrativo.
- Entrega del sitio Final.

## Observaciones adicionales

* El sistema ha sido probado con exito en ubn entorno WSL cumpliendo con la arquitectura de 3 capas.