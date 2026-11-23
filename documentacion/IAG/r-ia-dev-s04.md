# Reporte de desarrollo con IA — r-ia-dev-s04

**Proyecto:** GameBuildArchive  
**Integrante responsable:** Antonio de Jesús Tejeda López  
**Entrega:** 4 — Desarrollo del frontend  
**Sesión:** 04 — Implementación de imágenes y fondo de la wiki  
**Fecha de la sesión:** 20/11/2026  
**Herramienta utilizada:** ChatGPT (OpenAI)  
**Modelo:** GPT-6 (la temperatura no se muestra en la interfaz)  
**Código de uso:** Diseño / Generación de imagen / Modificación / Explicación / Revisión  
**Repositorio:** https://github.com/ICRURO/desarrollo-web

---

## 1. Motivo de la consulta

Para la cuarta entrega estábamos trabajando en la interfaz de GameBuildArchive. La página ya tenía una estructura básica, pero las tarjetas de videojuegos solo mostraban letras y bloques de colores. Quería que se pareciera más a una wiki de videojuegos, sin agregar demasiadas cosas ni utilizar tablas para el diseño.

La idea fue colocar una imagen en cada apartado de **Hearts of Iron IV**, **Zenless Zone Zero** y **Dying Light**. Después decidí agregar un fondo que combinara la temática de los tres juegos, porque el espacio que rodeaba la wiki se veía muy simple. Para realizarlo se utilizó IA como apoyo en el diseño visual y para revisar los cambios necesarios en HTML y CSS.

## 2. Prompts y respuestas de la IA

### Prompt 1. Diseño de las tarjetas

> Estoy haciendo la cuarta entrega de GameBuildArchive, una wiki de videojuegos. Quiero que la página tenga un diseño sencillo, como el de otras wikis, utilizando colores sólidos y sin hacer tablas para organizar todo. Ya tengo tres apartados de videojuegos: Hearts of Iron IV, Zenless Zone Zero y Dying Light. ¿Cómo puedo mostrar una imagen en cada tarjeta sin cambiar toda la estructura de la página? Indícame qué modificar en el HTML y en el CSS.

**Respuesta de la IA:**

Se recomendó conservar las tarjetas y sustituir los bloques que mostraban las iniciales de cada juego por etiquetas `<img>`. Las imágenes debían colocarse en `frontend/img/`, mientras que los estilos se controlarían mediante la clase `.imagen-juego`. De esta manera seguirían apareciendo el nombre y la descripción debajo de cada imagen, sin modificar los enlaces de las tarjetas.

**Ejemplo de HTML aplicado:**

```html
<a class="tarjeta-juego" href="#articulos" id="hoi4">
    <img src="img/hoi4.jpg" alt="Hearts of Iron IV" class="imagen-juego">
    <span class="tarjeta-cuerpo">
        <strong>Hearts of Iron IV</strong>
        <small>Estrategia e historia</small>
    </span>
</a>
```

En las otras tarjetas se utilizaron `img/zzz.jpg` e `img/dyinglight.jpg`.

### Prompt 2. Ajustar las imágenes a las tarjetas

> Ya coloqué las imágenes de los tres videojuegos en sus apartados, pero quiero que todas se vean del mismo tamaño para que el diseño no quede disparejo. ¿Qué estilo debo agregar para que las imágenes ocupen el ancho de cada tarjeta y no se deformen? También quiero que el nombre del videojuego siga apareciendo debajo.

**Respuesta de la IA:**

Se sugirió utilizar `width: 100%` para ocupar el ancho de la tarjeta, una altura fija y `object-fit: cover` para adaptar el recorte de la imagen sin estirarla. El texto seguiría dentro de `.tarjeta-cuerpo`.

**CSS aplicado:**

```css
.imagen-juego {
    width: 100%;
    height: 160px;
    display: block;
    object-fit: cover;
}
```

### Prompt 3. Imagen de fondo de la wiki

> Ahora quiero agregar un fondo a GameBuildArchive. En lugar de dejar solamente un color liso alrededor de la página, quiero una imagen que reúna la temática de Hearts of Iron IV, Zenless Zone Zero y Dying Light. La parte principal de la wiki debe seguir siendo legible. Ayúdame con la imagen y explícame cómo ponerla de fondo utilizando el CSS que ya tengo.

**Respuesta de la IA:**

Se trabajó en una imagen temática para el fondo y se indicó guardarla en `frontend/img/`. Para mostrarla detrás de la página se recomendó colocar `background-image` en el selector `body`, usar `background-size: cover`, centrar la imagen y evitar que se repitiera. También se conservó un contenedor principal con un fondo blanco ligeramente transparente para que el texto no perdiera legibilidad.

**CSS aplicado:**

```css
body {
    margin: 0;
    background-image: url("../img/fongo-wiki.png");
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    background-attachment: fixed;
    color: var(--texto);
    font-family: Arial, Helvetica, sans-serif;
    line-height: 1.5;
}

.estructura {
    display: grid;
    grid-template-columns: 224px minmax(0, 1fr);
    max-width: 1200px;
    margin: 30px auto;
    min-height: calc(100vh - 120px);
    background-color: rgba(255, 255, 255, 0.96);
}
```

### Prompt 4. Revisar por qué no aparecía el fondo

> Ya agregué el `background-image` al archivo `styles.css`, pero al abrir la página sigo sin ver el cambio. El archivo de imagen está dentro de la carpeta `img`. ¿Puedes revisar cómo se escriben las rutas relativas desde CSS y qué debo comprobar para que se muestre el fondo?

**Respuesta de la IA:**

Se revisó la ruta relativa, tomando en cuenta que `styles.css` está en `frontend/css/` y las imágenes en `frontend/img/`. Para regresar un nivel de carpeta se utiliza `../img/`. También se identificó que el archivo real se llamaba **`fongo-wiki.png`**, por lo que escribir `fondo-wiki.png` no encontraba la imagen. Se indicó conservar exactamente el nombre guardado en la carpeta y volver a cargar la página.

---

## 3. Código anterior y posterior

**Commit anterior:** [`6f83a4e`](https://github.com/ICRURO/desarrollo-web/commit/6f83a4e769e815dfd66d2de80c20fc64ddb61e20) — Implementar funciones Fetch y alertas de confirmación y error en frontend.  
**Commit posterior:** [`d6d5298`](https://github.com/ICRURO/desarrollo-web/commit/d6d5298a5df2740bc7ca5312f3cb528c81c667e1) — Implementacion de imagenes y un fondo.

**Antes:** Las tarjetas de los tres videojuegos tenían bloques de color con las abreviaturas `HOI IV`, `ZZZ` y `DL`. El fondo de la página era un color sólido.

**Después:** Las tarjetas muestran imágenes de cada videojuego y el `body` utiliza una imagen de fondo. También se ajustó el contenedor principal para que el contenido siguiera siendo legible.

**Archivos involucrados:**

- `frontend/index.html`
- `frontend/css/styles.css`
- `frontend/img/hoi4.jpg`
- `frontend/img/zzz.jpg`
- `frontend/img/dyinglight.jpg`
- `frontend/img/fongo-wiki.png`

## 4. Diferencias entre versiones

Para revisar las modificaciones desde la terminal WSL:

```bash
git diff --numstat 6f83a4e769e815dfd66d2de80c20fc64ddb61e20 d6d5298a5df2740bc7ca5312f3cb528c81c667e1
```

**Cambios registrados en el commit:**

| Archivo | Líneas agregadas | Líneas eliminadas |
|---|---:|---:|
| `frontend/css/styles.css` | 739 | 111 |
| `frontend/index.html` | 22 | 12 |
| `frontend/img/dyinglight.jpg` | Binario nuevo | — |
| `frontend/img/fongo-wiki.png` | Binario nuevo | — |
| `frontend/img/hoi4.jpg` | Binario nuevo | — |
| `frontend/img/zzz.jpg` | Binario nuevo | — |

El cambio del CSS incluye también reacomodo y formato de reglas, no únicamente las instrucciones para las imágenes. Los archivos JPG y PNG no se cuentan como líneas de texto en `git diff --numstat`.

## 5. Prueba de funcionamiento

Para comprobar la implementación, se revisaron los archivos que quedaron en el repositorio y las rutas usadas en HTML y CSS. El commit muestra las cuatro imágenes y sus referencias en los archivos correspondientes.

Para verificar visualmente la página desde WSL, se puede ejecutar desde la raíz del repositorio:

```bash
cd frontend
python3 -m http.server 5500
```

Después, abrir `http://localhost:5500` en el navegador y comprobar que:

1. Hearts of Iron IV, Zenless Zone Zero y Dying Light muestran sus imágenes en las tarjetas.
2. El fondo aparece alrededor de la estructura principal de la wiki.
3. El texto y los apartados siguen siendo legibles sobre el fondo de la página.
4. Las tarjetas conservan sus enlaces a la sección de artículos.

**Verificación de código y archivos:** Confirmada en el commit `d6d5298`.  
**Evidencia visual final:** Agregar una captura de la página abierta en el navegador. Esta captura no está adjunta todavía a este reporte.

**Ruta sugerida de evidencia:** `evidencias/wiki-imagenes-fondo.png` (agregar el archivo antes de enlazarlo en GitHub).

## 6. Reflexión del estudiante

Para empezar, quería que la página tuviera una apariencia más parecida a una wiki, porque las tarjetas con puras letras y colores se veían demasiado simples. Por eso agregamos una imagen para cada videojuego y después ajustamos sus tamaños con CSS para que no quedaran diferentes. Luego quise poner una imagen de fondo que combinara los tres juegos, pero al principio no se reflejaba el cambio. Al revisar la ruta nos dimos cuenta de que el nombre real del archivo era `fongo-wiki.png` y lo estaba confundiendo con `fondo-wiki.png`.

Esta parte me sirvió para entender mejor cómo se usan las rutas relativas entre las carpetas `css` e `img`, y cómo `object-fit` ayuda a acomodar las imágenes. También aprendí que no es necesario cambiar toda la página para mejorar su presentación; se pueden conservar las tarjetas y modificar únicamente las partes visuales que hacen falta.

---
