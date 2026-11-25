package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Role;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public Usuario cadastrar(Usuario usuario) {
        String email = usuario.getEmail().trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        usuario.setId(null);
        usuario.setEmail(email);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        // O cadastro público nunca pode escolher um perfil privilegiado.
        usuario.setRole(Role.USER);
        return repository.save(usuario);
    }
}
