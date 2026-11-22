document.addEventListener("DOMContentLoaded", () => {
  if (usuarioActual()) {
    window.location.href = "index.html";
    return;
  }

  const params = new URLSearchParams(window.location.search);
  if (params.get("registrado")) {
    mostrarMensaje("Cuenta creada correctamente. Ahora inicia sesión.", "exito");
  }

  document.getElementById("form-login").addEventListener("submit", iniciarSesion);
});

async function iniciarSesion(e) {
  e.preventDefault();

  const identificador = document.getElementById("identificador").value.trim();
  const password = document.getElementById("password").value;

  if (!identificador || !password) {
    mostrarMensaje("Escribe tu usuario o correo y tu contraseña.", "error");
    return;
  }

  const boton = document.getElementById("btn-login");
  boton.disabled = true;

  try {
    const resp = await fetch(`${API_BASE}/usuarios/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ identificador: identificador, password: password })
    });

    const datos = await resp.json().catch(() => ({}));

    if (resp.status === 400 || resp.status === 401) {
      mostrarMensaje(datos.mensaje || "Usuario o contraseña incorrectos.", "error");
      return;
    }
    if (!resp.ok) {
      throw new Error(`Error HTTP ${resp.status}`);
    }

    iniciarSesionLocal(datos);
    window.location.href = "index.html";
  } catch (error) {
    console.error(error);
    mostrarMensaje("No se pudo conectar con el servidor. Verifica que Spring Boot esté activo.", "error");
  } finally {
    boton.disabled = false;
  }
}