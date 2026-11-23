# Reporte de IA — Pruebas de funcionamiento de GameBuildArchive

**Proyecto:** GameBuildArchive — Comunidad de guías de videojuegos  
**Equipo:** 9  
**Integrante responsable:** José Fernando Martínez Enríquez  
**Fecha de la sesión:** 22/11/2026  
**Sesión de IA:** 04 — Pruebas de funcionamiento  
**Repositorio:** https://github.com/ICRURO/desarrollo-web  
**Entorno de ejecución:** Ubuntu / WSL, Spring Boot (puerto 8080), Frontend (puerto 5500), PostgreSQL y `curl`  
**Resultado:** 23 pruebas realizadas; 23 respuestas coinciden con las esperadas.

---

## 1. Breve descripción de uso

Para la última entrega del proyecto utilicé consultas a ChatGPT para organizar pruebas de funcionamiento desde WSL. Se revisaron los endpoints implementados en Spring Boot y se prepararon comandos `curl` para probar respuestas correctas y errores controlados. Después ejecuté los comandos en la terminal y revisé las salidas para verificar el código HTTP y los datos que devolvió la API. Las pruebas abarcaron registro, inicio de sesión, validaciones, videojuegos, publicaciones, filtros de búsqueda y comentarios.

## 2. Evidencias de uso

| Evidencia | Registro |
|---|---|
| Herramienta | ChatGPT (OpenAI) |
| Modelo y temperatura | GPT-6; la temperatura no se expone en la interfaz. |
| Prompt original | [Consulta inicial](#3-prompt-original) |
| Respuesta de IA | [Plan seguido](#4-respuesta-de-ia) |
| Código anterior | No aplica: no se cambió código fuente para realizar estas pruebas. |
| Código posterior | No aplica: se ejecutaron peticiones HTTP desde WSL. |
| Diferencias entre versiones | No aplica a esta sesión; sin cambios de código atribuibles a las pruebas. |
| Prueba de funcionamiento | [23 pruebas con comandos y resultados](#6-pruebas-ejecutadas) |
| Código de uso | Pruebas |
| Reflexión del estudiante | [Reflexión](#8-reflexión-del-estudiante) |

## 3. Prompt original

> mira para la ultima entrega tengo que hacer las pruebas de ia, solo que el profe no dejo una estructura y una compañera me dijo que ella midifico la de dev ia y pues vamos hacer lo mismo.

Se compartió además el repositorio de GitHub, el PDF del proyecto final y el archivo de referencia `r-ia-dev-pruebas-08.md` de una compañera. En los mensajes siguientes se solicitaron y ejecutaron los comandos de prueba uno por uno, y la IA revisó las respuestas de la terminal antes de continuar.

## 4. Respuesta de IA

La IA propuso adaptar el formato `r-ia-dev` para documentar pruebas de GameBuildArchive, tomando como referencia los endpoints que realmente aparecen en `UsuarioController`, `VideojuegoController`, `PublicacionController` y `ComentarioController`. Se acordó probar, mediante peticiones `curl`, respuestas de éxito y de error. La URL utilizada para la API fue `http://localhost:8080/api`.

Al principio, el primer intento de registro devolvió `curl: (7) Failed to connect to localhost port 8080` porque no se podía conectar con el backend. Se revisó cómo iniciar PostgreSQL, configurar `GAMEBUILD_DB_PASSWORD` y ejecutar Spring Boot. Una vez que estuvo disponible el servidor, se repitió el registro y devolvió HTTP 201. En otra ocasión pegué accidentalmente unas líneas del resultado esperado (`HTTP/1.1 200`, `Content-Type`, `[]`) en la terminal; Bash intentó ejecutarlas, así que se repitió la indicación y se corrió el comando `curl` correcto.

El criterio para aprobar cada caso fue comparar la respuesta real obtenida en la terminal con el código HTTP y el contenido esperado. Los códigos 400, 401 y 409 de las pruebas negativas se contaron como resultados correctos porque la solicitud debía ser rechazada.

## 5. Resumen de resultados

| Prueba | Caso | HTTP esperado | HTTP obtenido | Estado |
|---:|---|---:|---:|---|
| 1 | Registrar un usuario válido | 201 | 201 | Aprobada |
| 2 | Iniciar sesión con credenciales correctas | 200 | 200 | Aprobada |
| 3 | Intentar ingresar con contraseña incorrecta | 401 | 401 | Aprobada |
| 4 | Registrar un nombre de usuario duplicado | 409 | 409 | Aprobada |
| 5 | Registrar un correo electrónico duplicado | 409 | 409 | Aprobada |
| 6 | Registrar un correo con formato incorrecto | 400 | 400 | Aprobada |
| 7 | Registrar una contraseña demasiado corta | 400 | 400 | Aprobada |
| 8 | Registrar una contraseña sin números | 400 | 400 | Aprobada |
| 9 | Registrar un nombre de usuario de dos caracteres | 400 | 400 | Aprobada |
| 10 | Consultar videojuegos cuando la tabla está vacía | 200 | 200 | Aprobada |
| 11 | Crear un videojuego | 201 | 201 | Aprobada |
| 12 | Consultar el videojuego después de crearlo | 200 | 200 | Aprobada |
| 13 | Buscar un videojuego por parte del nombre | 200 | 200 | Aprobada |
| 14 | Consultar publicaciones cuando la tabla está vacía | 200 | 200 | Aprobada |
| 15 | Crear una publicación | 201 | 201 | Aprobada |
| 16 | Consultar la publicación creada | 200 | 200 | Aprobada |
| 17 | Filtrar publicaciones por título | 200 | 200 | Aprobada |
| 18 | Filtrar publicaciones por videojuego | 200 | 200 | Aprobada |
| 19 | Filtrar publicaciones por autor | 200 | 200 | Aprobada |
| 20 | Combinar título, videojuego y usuario | 200 | 200 | Aprobada |
| 21 | Crear un comentario | 201 | 201 | Aprobada |
| 22 | Consultar comentarios de una publicación | 200 | 200 | Aprobada |
| 23 | Intentar registrar un comentario vacío | 400 | 400 | Aprobada |

**Total:** 23 pruebas ejecutadas y 23 aprobadas según las respuestas comprobadas en la terminal. Los resultados positivos de esta selección de casos no implican que todos los módulos o controles de seguridad estén completamente probados.

## 6. Pruebas ejecutadas

Cada prueba corresponde a una ejecución real realizada en WSL.

### PRUEBA 1. Registrar un usuario válido

**Módulo:** Usuarios / registro  
**Objetivo:** Confirmar que el registro de una cuenta válida devuelve sus datos sin contraseña.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"pruebafer","correo":"pruebafer@example.com","password":"Prueba12345"}'
```

**Código esperado:** HTTP 201  
**Código obtenido:** HTTP 201  
**Resultado observado:** Se devolvió el usuario con `id: 1`, nombre `pruebafer` y correo `pruebafer@example.com`.  
**Evaluación:** Aprobada.

---

### PRUEBA 2. Iniciar sesión con credenciales correctas

**Módulo:** Usuarios / login  
**Objetivo:** Comprobar la autenticación de credenciales válidas por el endpoint de login.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{"identificador":"pruebafer","password":"Prueba12345"}'
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** Se recuperó el usuario `pruebafer` con `id: 1`.  
**Evaluación:** Aprobada.

---

### PRUEBA 3. Intentar ingresar con contraseña incorrecta

**Módulo:** Usuarios / login  
**Objetivo:** Comprobar que se rechazan credenciales incorrectas.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/login \
  -H "Content-Type: application/json" \
  -d '{"identificador":"pruebafer","password":"Incorrecta123"}'
```

**Código esperado:** HTTP 401  
**Código obtenido:** HTTP 401  
**Resultado observado:** Respuesta: `{"mensaje":"Usuario o contraseña incorrectos."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 4. Registrar un nombre de usuario duplicado

**Módulo:** Usuarios / registro  
**Objetivo:** Confirmar que el sistema evita nombres de usuario repetidos.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"pruebafer","correo":"otrocorreo@example.com","password":"Prueba12345"}'
```

**Código esperado:** HTTP 409  
**Código obtenido:** HTTP 409  
**Resultado observado:** Respuesta: `{"mensaje":"Ese nombre de usuario ya está en uso."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 5. Registrar un correo electrónico duplicado

**Módulo:** Usuarios / registro  
**Objetivo:** Confirmar la validación de correos repetidos.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"usuarioalternativo","correo":"pruebafer@example.com","password":"Prueba12345"}'
```

**Código esperado:** HTTP 409  
**Código obtenido:** HTTP 409  
**Resultado observado:** Respuesta: `{"mensaje":"Ese correo ya está registrado."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 6. Registrar un correo con formato incorrecto

**Módulo:** Usuarios / registro  
**Objetivo:** Comprobar la validación del formato de correo.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"usuarioinvalido","correo":"correoincorrecto","password":"Prueba12345"}'
```

**Código esperado:** HTTP 400  
**Código obtenido:** HTTP 400  
**Resultado observado:** Respuesta: `{"mensaje":"Escribe un correo con formato válido, por ejemplo nombre@dominio.com."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 7. Registrar una contraseña demasiado corta

**Módulo:** Usuarios / registro  
**Objetivo:** Comprobar la longitud mínima de contraseña.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"usuariocorto","correo":"usuariocorto@example.com","password":"Ab123"}'
```

**Código esperado:** HTTP 400  
**Código obtenido:** HTTP 400  
**Resultado observado:** Respuesta: `{"mensaje":"La contraseña debe tener entre 8 y 64 caracteres."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 8. Registrar una contraseña sin números

**Módulo:** Usuarios / registro  
**Objetivo:** Verificar el requisito de caracteres de la contraseña.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"usuariosinnumero","correo":"sinnumero@example.com","password":"SolamenteLetras"}'
```

**Código esperado:** HTTP 400  
**Código obtenido:** HTTP 400  
**Resultado observado:** Respuesta: `{"mensaje":"La contraseña debe incluir al menos una letra y un número."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 9. Registrar un nombre de usuario de dos caracteres

**Módulo:** Usuarios / registro  
**Objetivo:** Comprobar la longitud mínima del nombre de usuario.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{"nombreUsuario":"ab","correo":"usuariocorto2@example.com","password":"Prueba12345"}'
```

**Código esperado:** HTTP 400  
**Código obtenido:** HTTP 400  
**Resultado observado:** Respuesta: `{"mensaje":"El usuario debe tener de 3 a 50 caracteres: letras, números, punto, guion o guion bajo."}`.  
**Evaluación:** Aprobada.

---

### PRUEBA 10. Consultar videojuegos cuando la tabla está vacía

**Módulo:** Videojuegos  
**Objetivo:** Verificar una consulta de lista vacía.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/videojuegos"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La API devolvió `[]`; el endpoint respondió aunque todavía no había videojuegos.  
**Evaluación:** Aprobada.

---

### PRUEBA 11. Crear un videojuego

**Módulo:** Videojuegos  
**Objetivo:** Verificar creación y asignación de ID de un videojuego.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/videojuegos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Hearts of Iron IV","descripcion":"Videojuego de estrategia ambientado en la Segunda Guerra Mundial","genero":"Estrategia"}'
```

**Código esperado:** HTTP 201  
**Código obtenido:** HTTP 201  
**Resultado observado:** Se registró **Hearts of Iron IV** con `id: 1` y género `Estrategia`.  
**Evaluación:** Aprobada.

---

### PRUEBA 12. Consultar el videojuego después de crearlo

**Módulo:** Videojuegos  
**Objetivo:** Comprobar que el registro anterior se puede recuperar.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/videojuegos"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La lista incluyó **Hearts of Iron IV**, `id: 1`, con su descripción y género.  
**Evaluación:** Aprobada.

---

### PRUEBA 13. Buscar un videojuego por parte del nombre

**Módulo:** Videojuegos  
**Objetivo:** Probar la búsqueda parcial de videojuegos.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/videojuegos?nombre=Hearts"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La búsqueda de `Hearts` devolvió **Hearts of Iron IV**.  
**Evaluación:** Aprobada.

---

### PRUEBA 14. Consultar publicaciones cuando la tabla está vacía

**Módulo:** Publicaciones  
**Objetivo:** Confirmar la consulta de publicaciones antes de crear registros.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** Se obtuvo `[]`, sin errores en el endpoint.  
**Evaluación:** Aprobada.

---

### PRUEBA 15. Crear una publicación

**Módulo:** Publicaciones  
**Objetivo:** Verificar la creación de una publicación asociada a usuario y videojuego.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/publicaciones \
  -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"titulo":"Guía básica de Hearts of Iron IV","resumen":"Esta guía explica los primeros pasos para comenzar a jugar.","videojuego":{"id":1}}'
```

**Código esperado:** HTTP 201  
**Código obtenido:** HTTP 201  
**Resultado observado:** Se creó la publicación con `id: 1`, `usuarioId: 1` y `videojuego.id: 1`. En la respuesta inmediata algunos atributos anidados del videojuego aparecieron `null`.  
**Evaluación:** Aprobada.

---

### PRUEBA 16. Consultar la publicación creada

**Módulo:** Publicaciones  
**Objetivo:** Comprobar la recuperación de datos y la relación entre publicación y videojuego.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La respuesta recuperó la publicación `id: 1` y los datos completos del videojuego **Hearts of Iron IV**.  
**Evaluación:** Aprobada.

---

### PRUEBA 17. Filtrar publicaciones por título

**Módulo:** Publicaciones / filtros  
**Objetivo:** Comprobar el filtro de coincidencia parcial por título.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones?titulo=Hearts"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La lista incluyó **Guía básica de Hearts of Iron IV**.  
**Evaluación:** Aprobada.

---

### PRUEBA 18. Filtrar publicaciones por videojuego

**Módulo:** Publicaciones / filtros  
**Objetivo:** Comprobar el filtro por identificador de videojuego.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones?videojuegoId=1"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La lista incluyó la publicación correspondiente al videojuego `id: 1`.  
**Evaluación:** Aprobada.

---

### PRUEBA 19. Filtrar publicaciones por autor

**Módulo:** Publicaciones / filtros  
**Objetivo:** Comprobar el filtro por identificador de usuario.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones?usuarioId=1"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La lista incluyó la publicación registrada por el usuario `id: 1`.  
**Evaluación:** Aprobada.

---

### PRUEBA 20. Combinar título, videojuego y usuario

**Módulo:** Publicaciones / filtros  
**Objetivo:** Comprobar la aplicación simultánea de tres filtros.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/publicaciones?titulo=Hearts&videojuegoId=1&usuarioId=1"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** La consulta devolvió la publicación que cumplía los tres filtros.  
**Evaluación:** Aprobada.

---

### PRUEBA 21. Crear un comentario

**Módulo:** Comentarios  
**Objetivo:** Verificar el registro de un comentario asociado a una publicación.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/comentarios \
  -H "Content-Type: application/json" \
  -d '{"publicacionId":1,"usuarioId":1,"contenido":"Esta guía me ayudó a comprender los primeros pasos del juego."}'
```

**Código esperado:** HTTP 201  
**Código obtenido:** HTTP 201  
**Resultado observado:** Se creó un comentario con `id: 1`, `publicacionId: 1` y `usuarioId: 1`.  
**Evaluación:** Aprobada.

---

### PRUEBA 22. Consultar comentarios de una publicación

**Módulo:** Comentarios  
**Objetivo:** Comprobar que el comentario se puede recuperar por su publicación.

**Comando `curl`:**

```bash
curl -i -X GET "http://localhost:8080/api/comentarios?publicacionId=1"
```

**Código esperado:** HTTP 200  
**Código obtenido:** HTTP 200  
**Resultado observado:** Se recuperó el comentario `id: 1` con el texto "Esta guía me ayudó a comprender los primeros pasos del juego."  
**Evaluación:** Aprobada.

---

### PRUEBA 23. Intentar registrar un comentario vacío

**Módulo:** Comentarios  
**Objetivo:** Comprobar que no se acepta un comentario sin contenido.

**Comando `curl`:**

```bash
curl -i -X POST http://localhost:8080/api/comentarios \
  -H "Content-Type: application/json" \
  -d '{"publicacionId":1,"usuarioId":1,"contenido":""}'
```

**Código esperado:** HTTP 400  
**Código obtenido:** HTTP 400  
**Resultado observado:** La solicitud fue rechazada: `HTTP/1.1 400` y `Content-Length: 0` (sin cuerpo de respuesta).  
**Evaluación:** Aprobada.

---

## 7. Observaciones y alcance

- El primer intento de `curl` falló porque el servidor no estaba accesible en el puerto 8080. Después de iniciar los servicios y ejecutar Spring Boot, el endpoint respondió correctamente.
- Las pruebas 10 y 14 devolvieron listas vacías (`[]`) porque aún no se habían registrado videojuegos ni publicaciones. Las consultas funcionaron y después se crearon registros para continuar.
- En la prueba 15, el objeto `videojuego` de la respuesta inmediata del POST mostraba los campos descriptivos como `null` y el identificador correcto. En la prueba 16 se consultó de nuevo y se recuperaron los datos completos del videojuego.
- En la prueba 23, el HTTP 400 venía sin cuerpo (`Content-Length: 0`), lo que coincide con el método de validación del controlador de comentarios.
- **Limitación técnica:** las pruebas `curl` muestran el comportamiento de estos endpoints. No prueban que el servidor restrinja operaciones según el usuario autenticado. En la implementación revisada, el frontend conserva al usuario en `localStorage` y no existe una sesión autenticada de servidor para autorizar todas las operaciones de publicación y comentario. Conviene tratarlo como una mejora pendiente, no describirlo como protección verificada.
- Las pruebas sí agregaron datos a PostgreSQL (usuario, videojuego, publicación y comentario de prueba). No se ejecutaron pruebas de eliminación ni limpieza en esta sesión; estos registros permanecen hasta que alguien los elimine.

## 8. Reflexión del estudiante

Para empezar, tuve que comprobar que el backend estuviera funcionando porque la primera vez que ejecuté `curl` me salió un error de conexión. Después de iniciar el servidor pude registrar un usuario y hacer el inicio de sesión con sus datos. También probé diferentes errores, como escribir mal la contraseña, repetir el usuario o correo y usar datos que no cumplían las validaciones.

Luego pasé a videojuegos, publicaciones y comentarios. Al principio las consultas devolvían listas vacías porque todavía no había registrado información, así que fui creando los datos para poder continuar. Con las respuestas de la terminal revisé que los códigos HTTP y los datos coincidieran con lo que se esperaba. Así pude entender mejor cómo responde la API cuando una petición se realiza correctamente y cuando tiene datos incorrectos.

Al final conseguí completar las 23 pruebas propuestas. Me sirvió para comprobar los endpoints desde WSL y tener el registro para la última entrega, aunque todavía queda revisar por separado la seguridad de las operaciones que modifican datos.

