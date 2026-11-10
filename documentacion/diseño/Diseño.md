**Nombre:** GameBuildArchive   

**Descripción:** GameBuildArchive es una plataforma web orientada a la comunidad de jugadores que permite la creación, consulta y gestión de información sobre videojuegos. El sistema está diseñado para que los usuarios puedan documentar y compartir sus propias configuraciones de personajes, builds y guías estratégicas. A través de un entorno colaborativo, la comunidad puede buscar guías por categorías, interactuar mediante comentarios y valorar aportes..

## 1.1 Crear repositorio GitHub

Siguiendo lo señalado en el documento del proyecto final se hizo el repositorio git añadiendo el webhook dado para el curso.

## 1.2 Definir requerimientos

**Requerimientos funcionales**

|ID|Requerimiento|Prioridad|
|----|-----------------|-------|
|RF-01|El sistema permite registrar usuarios.|Alta|
|RF-02|El sistema permite iniciar/cerrar sesion.|Alta|
|RF-03|El sistema permite consultar perfil de usuario.|Media|
|RF-04|El sistema permite moficiar/eliminar usuario.|Media|
|RF-05|El sistema muestra un catálogo de videojuegos guardados.|Alta|
|RF-06|El usuario puede consultar publicaciones del catálogo.|Media|
|RF-07|El sistema permite busquedas por nombre/categoría.|Media|
|RF-08|El usuario puede consultar publicaciones/guías de la comunidad.|Alta|
|RF-09|El usuario puede crear publicaciones.|Alta|
|RF-10|Las publicaciones deben tener titulo, autor, videojuego y cuerpo de la publicación.|Media|
|RF-11|El autor puede modificar sus publicaciones.|Alta|
|RF-12|El autor puede eliminar sus publicaciones.|Media|
|RF-13|El sistema peemite el uso de markdown e imagenes|Alta|
|RF-14|El sistema puede filtrar contenido por categorias/etiquetas|Media|
|RF-15|El usuario puede comentar publicaciones|Media|
|RF-16|El usuario puede consultar los comentarios de una publicación|Media|
|RF-17|El usuario puede dar "Like" a un publicación|Baja|
|RF-18|El usuario puede consultar publicaciones por autor|Media|
|RF-19|El sistema tiene herramientas especificas para determinados videojuegos.|Baja|
|RF-20|El usuarip puede crear/consultar configuraciones de personajes/estrategias.|Baja|
|RF-21|El sistema puede guardar plantillas personalizadas|Muy baja|

**Requerimientos no funcionales**

|ID|Requisito no funcional|
|--|-------------------------|
|RN-01|El frontend debe desarrollarse en HTML, CSS y Js.|
|RN-02|El backend debe implementarse en Java y Spring Boot.|
|RN-03|El sistema utiliza PostgreSQL para la base de datos.|
|RN-04|EL front end y el backend se comunican mediante una API REST y Fetch.|
|RN-05|El sistema usa una arquitectura de 3 capas.|
|RN-06|El proyecto se desarrolla y ejecuta en WSL.|
|RN-07|El backend debe validar los datos recibidos antes de almacenar.|
|RN-08|Las contraseñas se deben proteger con hash.|
|RN-09|El sistema debe contar con roles definidos por sus privilegios.|

## 1.3 Definir de endpoints REST

Para el proyecto se implementará una API REST usando Java y Spring Boot, probada inicialmente con CURL, con la cual el frontend y la base de datos se comunicarán de manera asíncrona. Se desarrollarán métodos GET, POST, PUT y DELETE para el catálogo de videojuegos y las guías/builds publicadas.

Aún queda pendiente definir con exactitud el JSON y la estructura de los payloads para una publicación en su diseño final, asegurando que soporte el formato Markdown y el enlace a imágenes requerido

## 1.4 Distribuir responsabilidades

Para esta entrega el esfuerzo de describir el sitio web y sus objetivos/requerimientos fue grupal, definimos a un encargado de bases de datos, un responsable de frontend y los tres integrantes serán responsables del backend por las complicaciones que puedan presentarse al considerarse el punto más fuerte del proeycto. La creación de los reportes es una responsabilidad que será rotada entre entregas.

## 1.5 Creacion de roles en el sistema
Administrador: Tiene acceso total al sistema. Puede gestionar el catálogo de videojuegos base, moderar a la comunidad (eliminar publicaciones o comentarios que incumplan las normas) y modificar o eliminar cuentas de usuario (RF-04).

Usuario Registrado (Autor): Puede iniciar sesión, crear, editar y eliminar sus propias publicaciones, configuraciones o guías (RF-09, RF-11, RF-12). También puede interactuar con la comunidad dejando comentarios y dando "Like" a las publicaciones de otros.

Invitado (No Registrado): Solo tiene permisos de lectura. Puede navegar por el catálogo de videojuegos (RF-05), buscar publicaciones mediante filtros (RF-07, RF-14) y leer las guías, pero debe registrarse para crear contenido o interactuar.

