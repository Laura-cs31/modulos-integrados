package com.mycompany.modulos_integrados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Error: El correo ya está registrado.");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Registro exitoso.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Usuario existingUsuario = usuarioRepository.findByEmail(usuario.getEmail());
        if (existingUsuario == null || !passwordEncoder.matches(usuario.getPassword(), existingUsuario.getPassword())) {
            return ResponseEntity.badRequest().body("Error: Credenciales incorrectas.");
        }
        return ResponseEntity.ok("Inicio de sesión exitoso.");
    }
}
