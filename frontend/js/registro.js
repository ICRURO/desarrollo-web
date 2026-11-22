const PATRON_USUARIO = /^[A-Za-z0-9_.-]{3,50}$/;
const PATRON_CORREO = /^[A-Za-z0-9._%+-]{1,64}@([A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?\.)+[A-Za-z]{2,}$/;

function correoValido(correo) {
  if (correo.length > 150 || !PATRON_CORREO.test(correo) || correo.includes("..")) return false;
  const parteLocal = correo.split("@")[0];
  return !parteLocal.startsWith(".") && !parteLocal.endsWith(".");
}

document.addEventListener("DOMContentLoaded", () => {
  if (usuarioActual()) {
    window.location.href = "index.html";
    return;
  }

  document.getElementById("form-registro").addEventListener("submit", registrarUsuario);
});

async function registrarUsuario(e) {
  e.preventDefault();

  const nombreUsuario = document.getElementById("nombre-usuario").value.trim();
  const correo = document.getElementById("correo").value.trim();
  const password = document.getElementById("password").value;
  const confirmar = document.getElementById("confirmar-password").value;

  if (!PATRON_USUARIO.test(nombreUsuario)) {
    mostrarMensaje("El usuario debe tener de 3 a 50 caracteres: letras, números, punto, guion o guion bajo.", "error");
    return;
  }
  if (!correoValido(correo)) {
    mostrarMensaje("Escribe un correo con formato válido, por ejemplo nombre@dominio.com.", "error");
    return;
  }
  if (password.length < 8 || password.length > 64) {
    mostrarMensaje("La contraseña debe tener entre 8 y 64 caracteres.", "error");
    return;
  }
  if (!/[A-Za-z]/.test(password) || !/[0-9]/.test(password)) {
    mostrarMensaje("La contraseña debe incluir al menos una letra y un número.", "error");
    return;
  }
  if (password !== confirmar) {
    mostrarMensaje("Las contraseñas no coinciden.", "error");
    return;
  }

  const boton = document.getElementById("btn-registro");
  boton.disabled = true;

  try {
    const resp = await fetch(`${API_BASE}/usuarios/registro`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ nombreUsuario: nombreUsuario, correo: correo, password: password })
    });

    if (resp.status === 201) {
      window.location.href = "login.html?registrado=1";
      return;
    }

    const datos = await resp.json().catch(() => ({}));

    if (resp.status === 400) {
      mostrarMensaje(datos.mensaje || "Datos inválidos (HTTP 400).", "error");
    } else if (resp.status === 409) {
      mostrarMensaje(datos.mensaje || "El usuario o el correo ya están registrados (HTTP 409).", "error");
    } else {
      throw new Error(`Error HTTP ${resp.status}`);
    }
  } catch (error) {
    console.error(error);
    mostrarMensaje("No se pudo conectar con el servidor. Verifica que Spring Boot esté activo.", "error");
  } finally {
    boton.disabled = false;
  }
}
