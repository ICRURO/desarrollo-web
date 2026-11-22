package com.gamebuildarchive.backend.controller;

import com.gamebuildarchive.backend.entity.Usuario;
import com.gamebuildarchive.backend.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    public record RegistroRequest(String nombreUsuario, String correo, String password) {
    }

    public record LoginRequest(String identificador, String password) {
    }

    public record UsuarioRespuesta(Long id, String nombreUsuario, String correo) {
    }

    public record UsuarioPublico(Long id, String nombreUsuario) {
    }

    private static final Pattern PATRON_USUARIO = Pattern.compile("^[A-Za-z0-9_.-]{3,50}$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+-]{1,64}@([A-Za-z0-9]([A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$");
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioPublico>> listarUsuarios() {
        List<UsuarioPublico> usuarios = usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioPublico(u.getId(), u.getNombreUsuario()))
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest datos) {
        String nombre = datos.nombreUsuario() == null ? "" : datos.nombreUsuario().trim();
        String correo = datos.correo() == null ? "" : datos.correo().trim().toLowerCase(Locale.ROOT);
        String password = datos.password() == null ? "" : datos.password();
        if (!PATRON_USUARIO.matcher(nombre).matches()) {
            return error(HttpStatus.BAD_REQUEST,
                    "El usuario debe tener de 3 a 50 caracteres: letras, números, punto, guion o guion bajo.");
        }
        if (!correoValido(correo)) {
            return error(HttpStatus.BAD_REQUEST, "Escribe un correo con formato válido, por ejemplo nombre@dominio.com.");
        }
        String errorPassword = validarPassword(password);
        if (errorPassword != null) {
            return error(HttpStatus.BAD_REQUEST, errorPassword);
        }
        if (usuarioRepository.existsByNombreUsuarioIgnoreCase(nombre)) {
            return error(HttpStatus.CONFLICT, "Ese nombre de usuario ya está en uso.");
        }
        if (usuarioRepository.existsByCorreoIgnoreCase(correo)) {
            return error(HttpStatus.CONFLICT, "Ese correo ya está registrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setCorreo(correo);
        usuario.setPasswordHash(passwordEncoder.encode(password)); 
        try {
            Usuario guardado = usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta(guardado));
        } catch (DataIntegrityViolationException e) {
            return error(HttpStatus.CONFLICT, "El usuario o el correo ya están registrados.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequest datos) {
        String identificador = datos.identificador() == null ? "" : datos.identificador().trim();
        String password = datos.password() == null ? "" : datos.password();
        if (identificador.isEmpty() || password.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "Escribe usuario o correo y tu contraseña.");
        }
        Optional<Usuario> encontrado = identificador.contains("@")
                ? usuarioRepository.findByCorreoIgnoreCase(identificador)
                : usuarioRepository.findByNombreUsuarioIgnoreCase(identificador);
        boolean valido = encontrado.isPresent()
                && password.getBytes(StandardCharsets.UTF_8).length <= 72
                && passwordEncoder.matches(password, encontrado.get().getPasswordHash());
        if (!valido) {
            return error(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos.");
        }
        return ResponseEntity.ok(respuesta(encontrado.get()));
    }
    private boolean correoValido(String correo) {
        if (correo.length() > 150 || !PATRON_CORREO.matcher(correo).matches() || correo.contains("..")) {
            return false;
        }
        String parteLocal = correo.substring(0, correo.indexOf('@'));
        return !parteLocal.startsWith(".") && !parteLocal.endsWith(".");
    }

    private String validarPassword(String password) {
        if (password.length() < 8 || password.length() > 64) {
            return "La contraseña debe tener entre 8 y 64 caracteres.";
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            return "La contraseña es demasiado larga: usa menos símbolos especiales.";
        }
        if (password.chars().noneMatch(Character::isLetter) || password.chars().noneMatch(Character::isDigit)) {
            return "La contraseña debe incluir al menos una letra y un número.";
        }
        return null;
    }

    private UsuarioRespuesta respuesta(Usuario usuario) {
        return new UsuarioRespuesta(usuario.getId(), usuario.getNombreUsuario(), usuario.getCorreo());
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(Map.of("mensaje", mensaje));
    }
}