const API_BASE = "http://localhost:8080/api";
const CLAVE_SESION = "gba_usuario";

function usuarioActual() {
  try {
    const guardado = localStorage.getItem(CLAVE_SESION);
    return guardado ? JSON.parse(guardado) : null;
  } catch (error) {
    return null;
  }
}

function iniciarSesionLocal(usuario) {
  localStorage.setItem(CLAVE_SESION, JSON.stringify(usuario));
}

function cerrarSesion() {
  localStorage.removeItem(CLAVE_SESION);
  window.location.href = "index.html";
}

function mostrarMensaje(texto, tipo = "exito") {
  const alerta = document.getElementById("alerta-sistema");
  if (!alerta) return;

  alerta.textContent = texto;
  alerta.className = `alerta ${tipo === "exito" ? "alerta-exito" : "alerta-error"}`;

  clearTimeout(alerta._temporizador);
  alerta._temporizador = setTimeout(() => {
    alerta.className = "alerta-oculta";
  }, 5000);
}

function pintarSesionEnCabecera() {
  const zona = document.getElementById("zona-sesion");
  if (!zona) return;

  zona.innerHTML = "";
  const usuario = usuarioActual();

  if (usuario) {
    const saludo = document.createElement("span");
    saludo.className = "sesion-usuario";
    saludo.append("Hola, ");
    const nombre = document.createElement("strong");
    nombre.textContent = usuario.nombreUsuario;
    saludo.appendChild(nombre);

    const boton = document.createElement("button");
    boton.type = "button";
    boton.className = "boton-salir";
    boton.textContent = "Salir";
    boton.addEventListener("click", cerrarSesion);

    zona.append(saludo, boton);
  } else {
    const entrar = document.createElement("a");
    entrar.href = "login.html";
    entrar.textContent = "Iniciar sesión";

    const registro = document.createElement("a");
    registro.href = "registro.html";
    registro.textContent = "Registrarse";

    zona.append(entrar, registro);
  }
}

document.addEventListener("DOMContentLoaded", pintarSesionEnCabecera);