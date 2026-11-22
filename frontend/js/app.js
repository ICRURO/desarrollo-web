let publicacionesCargadas = [];
let nombresUsuarios = {};

document.addEventListener("DOMContentLoaded", async () => {
  await cargarUsuarios();
  actualizarFormularioSegunSesion();
  cargarVideojuegos();
  cargarPublicaciones();
  configurarEventos();
});

async function cargarUsuarios() {
  try {
    const resp = await fetch(`${API_BASE}/usuarios`);
    if (!resp.ok) throw new Error(`Error HTTP ${resp.status} al cargar usuarios`);

    const usuarios = await resp.json();
    nombresUsuarios = {};
    usuarios.forEach((u) => {
      nombresUsuarios[u.id] = u.nombreUsuario;
    });
  } catch (error) {
    console.error(error);
  }
}

function nombreDeUsuario(id) {
  return nombresUsuarios[id] || `Usuario #${id}`;
}

function actualizarFormularioSegunSesion() {
  const form = document.getElementById("form-publicacion");
  const aviso = document.getElementById("aviso-sesion");
  const conSesion = Boolean(usuarioActual());

  form.style.display = conSesion ? "" : "none";
  aviso.style.display = conSesion ? "none" : "block";
}

async function cargarVideojuegos() {
  const select = document.getElementById("videojuego");
  try {
    const resp = await fetch(`${API_BASE}/videojuegos`);
    if (!resp.ok) throw new Error(`Error HTTP ${resp.status} al cargar videojuegos`);

    const juegos = await resp.json();
    select.innerHTML = '<option value="">Selecciona un videojuego</option>';

    juegos.forEach((juego) => {
      const opcion = document.createElement("option");
      opcion.value = juego.id;
      opcion.textContent = juego.nombre;
      select.appendChild(opcion);
    });
  } catch (error) {
    console.error(error);
    mostrarMensaje("No se pudieron cargar los videojuegos desde el servidor.", "error");
  }
}

async function cargarPublicaciones(filtros = {}) {
  const contenedor = document.querySelector(".lista-articulos");
  contenedor.innerHTML = '<p class="texto-secundario" style="padding:16px;">Cargando publicaciones...</p>';

  const params = new URLSearchParams();
  if (filtros.titulo) params.append("titulo", filtros.titulo);
  if (filtros.videojuegoId) params.append("videojuegoId", filtros.videojuegoId);

  const url = params.toString()
    ? `${API_BASE}/publicaciones?${params.toString()}`
    : `${API_BASE}/publicaciones`;

  try {
    const resp = await fetch(url);
    if (!resp.ok) throw new Error(`Error HTTP ${resp.status} al obtener guías`);

    const publicaciones = await resp.json();
    renderizarPublicaciones(publicaciones);
  } catch (error) {
    console.error(error);
    contenedor.innerHTML = '<p class="texto-secundario" style="padding:16px; color:#9d3a3a;">Error al conectar con la API de publicaciones.</p>';
    mostrarMensaje("Error al cargar las publicaciones. Verifica que Spring Boot esté activo.", "error");
  }
}

function renderizarPublicaciones(publicaciones) {
  const contenedor = document.querySelector(".lista-articulos");
  contenedor.innerHTML = "";
  publicacionesCargadas = publicaciones;

  if (publicaciones.length === 0) {
    contenedor.innerHTML = '<p class="texto-secundario" style="padding:16px;">No hay publicaciones que coincidan con la búsqueda.</p>';
    return;
  }

  const usuario = usuarioActual();

  publicaciones.forEach((pub) => {
    const nombreJuego = pub.videojuego ? pub.videojuego.nombre : "General";
    const esAutor = Boolean(usuario) && usuario.id === pub.usuarioId;

    const botonesAutor = esAutor
      ? `<button type="button" class="btn-mini" onclick="prepararEdicion(${pub.id})">Editar</button>
         <button type="button" class="btn-mini btn-peligro" onclick="eliminarPublicacion(${pub.id})">Eliminar</button>`
      : "";

    const formularioComentario = usuario
      ? `<form class="form-comentario" onsubmit="enviarComentario(event, ${pub.id})">
           <input type="text" id="input-comentario-${pub.id}" placeholder="Escribe una observación o comentario..." required>
           <button type="submit" class="btn-mini">Comentar</button>
         </form>`
      : '<small>Para comentar, <a href="login.html">inicia sesión</a>.</small>';

    const article = document.createElement("article");
    article.className = "articulo";
    article.innerHTML = `
      <div class="articulo-cabecera">
        <div>
          <span class="categoria-articulo">${escaparHTML(nombreJuego)}</span>
          <h3>${escaparHTML(pub.titulo)}</h3>
          <small class="autor-articulo">Por ${escaparHTML(nombreDeUsuario(pub.usuarioId))}</small>
          <p>${escaparHTML(pub.resumen || "Sin resumen disponible.")}</p>
        </div>
      </div>
      <div class="articulo-acciones">
        ${botonesAutor}
        <button type="button" class="btn-mini" onclick="toggleComentarios(${pub.id})">Observaciones</button>
      </div>
      <div id="comentarios-box-${pub.id}" class="caja-comentarios" style="display:none;">
        <div id="lista-comentarios-${pub.id}" class="lista-comentarios"></div>
        ${formularioComentario}
      </div>
    `;
    contenedor.appendChild(article);
  });
}

async function guardarPublicacion(e) {
  e.preventDefault();

  if (!usuarioActual()) {
    mostrarMensaje("Debes iniciar sesión para publicar una guía.", "error");
    return;
  }

  const idEdicion = document.getElementById("publicacion-id").value;
  const titulo = document.getElementById("titulo").value.trim();
  const videojuegoId = document.getElementById("videojuego").value;
  const resumen = document.getElementById("resumen").value.trim();

  if (!titulo || !videojuegoId) {
    mostrarMensaje("El título y el videojuego son obligatorios.", "error");
    return;
  }

  const payload = {
    titulo: titulo,
    resumen: resumen,
    usuarioId: usuarioActual().id,
    videojuego: { id: Number(videojuegoId) }
  };

  const esEdicion = Boolean(idEdicion);
  const url = esEdicion ? `${API_BASE}/publicaciones/${idEdicion}` : `${API_BASE}/publicaciones`;
  const metodo = esEdicion ? "PUT" : "POST";

  try {
    const resp = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (resp.status === 400) {
      mostrarMensaje("Datos inválidos: verifica los campos enviados (HTTP 400).", "error");
      return;
    }
    if (resp.status === 404) {
      mostrarMensaje("No se encontró la publicación o el videojuego (HTTP 404).", "error");
      return;
    }
    if (!resp.ok) {
      throw new Error(`Error HTTP ${resp.status}`);
    }

    mostrarMensaje(
      esEdicion ? "Guía actualizada correctamente (HTTP 200)." : "Guía publicada con éxito (HTTP 201).",
      "exito"
    );
    limpiarFormulario();
    cargarPublicaciones();
  } catch (error) {
    console.error(error);
    mostrarMensaje("Ocurrió un error al guardar la publicación en el servidor.", "error");
  }
}

function prepararEdicion(id) {
  const pub = publicacionesCargadas.find((p) => p.id === id);
  if (!pub) return;

  document.getElementById("publicacion-id").value = pub.id;
  document.getElementById("titulo").value = pub.titulo;
  document.getElementById("resumen").value = pub.resumen || "";
  document.getElementById("videojuego").value = pub.videojuego ? pub.videojuego.id : "";

  document.getElementById("titulo-crear").textContent = `Editando guía #${id}`;
  document.getElementById("btn-submit-publicacion").textContent = "Guardar cambios";
  document.getElementById("crear-guia").scrollIntoView({ behavior: "smooth" });
}

async function eliminarPublicacion(id) {
  if (!confirm(`¿Seguro que deseas eliminar la guía #${id}?`)) return;

  try {
    const resp = await fetch(`${API_BASE}/publicaciones/${id}`, {
      method: "DELETE"
    });

    if (resp.status === 204 || resp.ok) {
      mostrarMensaje(`Guía #${id} eliminada correctamente (HTTP 204).`, "exito");
      cargarPublicaciones();
    } else if (resp.status === 404) {
      mostrarMensaje(`La guía #${id} ya no existe (HTTP 404).`, "error");
    } else {
      throw new Error(`Error HTTP ${resp.status}`);
    }
  } catch (error) {
    console.error(error);
    mostrarMensaje("No se pudo eliminar la publicación.", "error");
  }
}

function limpiarFormulario() {
  document.getElementById("form-publicacion").reset();
  document.getElementById("publicacion-id").value = "";
  document.getElementById("titulo-crear").textContent = "Crear una guía";
  document.getElementById("btn-submit-publicacion").textContent = "Publicar guía";
}

async function toggleComentarios(publicacionId) {
  const box = document.getElementById(`comentarios-box-${publicacionId}`);
  const visible = box.style.display === "block";
  box.style.display = visible ? "none" : "block";

  if (!visible) {
    await cargarComentarios(publicacionId);
  }
}

async function cargarComentarios(publicacionId) {
  const lista = document.getElementById(`lista-comentarios-${publicacionId}`);
  lista.innerHTML = "<small>Cargando observaciones...</small>";

  try {
    const resp = await fetch(`${API_BASE}/comentarios?publicacionId=${publicacionId}`);
    if (!resp.ok) throw new Error(`HTTP ${resp.status}`);

    const comentarios = await resp.json();
    if (comentarios.length === 0) {
      lista.innerHTML = "<small>Aún no hay observaciones para esta guía.</small>";
      return;
    }

    const usuario = usuarioActual();

    lista.innerHTML = comentarios
      .map((c) => {
        const botonEliminar = usuario && usuario.id === c.usuarioId
          ? `<button type="button" class="btn-mini btn-peligro" onclick="eliminarComentario(${c.id}, ${publicacionId})">×</button>`
          : "";
        return `
        <div class="item-comentario">
          <span><strong>${escaparHTML(nombreDeUsuario(c.usuarioId))}:</strong> ${escaparHTML(c.contenido)}</span>
          ${botonEliminar}
        </div>`;
      })
      .join("");
  } catch (error) {
    console.error(error);
    lista.innerHTML = '<small style="color:#9d3a3a;">Error al cargar observaciones.</small>';
  }
}

async function enviarComentario(e, publicacionId) {
  e.preventDefault();
  const input = document.getElementById(`input-comentario-${publicacionId}`);
  const contenido = input.value.trim();
  if (!contenido) return;

  try {
    const resp = await fetch(`${API_BASE}/comentarios`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        publicacionId: publicacionId,
        usuarioId: usuarioActual().id,
        contenido: contenido
      })
    });

    if (resp.status === 201) {
      input.value = "";
      mostrarMensaje("Observación agregada (HTTP 201).", "exito");
      await cargarComentarios(publicacionId);
    } else if (resp.status === 400) {
      mostrarMensaje("El comentario no puede estar vacío (HTTP 400).", "error");
    } else {
      throw new Error(`HTTP ${resp.status}`);
    }
  } catch (error) {
    console.error(error);
    mostrarMensaje("Error al enviar la observación.", "error");
  }
}

async function eliminarComentario(comentarioId, publicacionId) {
  try {
    const resp = await fetch(`${API_BASE}/comentarios/${comentarioId}`, {
      method: "DELETE"
    });

    if (resp.status === 204 || resp.ok) {
      mostrarMensaje("Observación eliminada (HTTP 204).", "exito");
      await cargarComentarios(publicacionId);
    } else if (resp.status === 404) {
      mostrarMensaje("La observación ya no existe (HTTP 404).", "error");
      await cargarComentarios(publicacionId);
    } else {
      throw new Error(`HTTP ${resp.status}`);
    }
  } catch (error) {
    console.error(error);
    mostrarMensaje("Error al eliminar la observación.", "error");
  }
}

function configurarEventos() {
  const form = document.getElementById("form-publicacion");
  form.addEventListener("submit", guardarPublicacion);

  document.getElementById("btn-cancelar-edicion").addEventListener("click", limpiarFormulario);

  const inputFiltro = document.getElementById("filtro-titulo");
  if (inputFiltro) {
    inputFiltro.addEventListener("input", (e) => {
      cargarPublicaciones({ titulo: e.target.value.trim() });
    });
  }

  const btnLimpiar = document.getElementById("btn-limpiar-filtro");
  if (btnLimpiar) {
    btnLimpiar.addEventListener("click", () => {
      if (inputFiltro) inputFiltro.value = "";
      cargarPublicaciones();
    });
  }

  document.querySelectorAll(".tarjeta-juego").forEach((tarjeta) => {
    tarjeta.addEventListener("click", async () => {
      const nombreJuego = tarjeta.querySelector("strong").textContent.trim();
      try {
        const resp = await fetch(`${API_BASE}/videojuegos?nombre=${encodeURIComponent(nombreJuego)}`);
        const juegos = await resp.json();
        if (juegos.length > 0) {
          cargarPublicaciones({ videojuegoId: juegos[0].id });
          mostrarMensaje(`Mostrando guías de ${juegos[0].nombre}`, "exito");
        }
      } catch (err) {
        console.error(err);
      }
    });
  });
}

function escaparHTML(texto) {
  const div = document.createElement("div");
  div.textContent = texto;
  return div.innerHTML;
}