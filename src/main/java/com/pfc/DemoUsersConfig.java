package com.pfc;

import com.pfc.thindesk.entity.Role;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoUsersConfig {

    // Contas de demonstração opcionais, somente se as variáveis forem configuradas.
    @Bean
    public CommandLineRunner criarContasDeTeste(UsuarioRepository repository, PasswordEncoder encoder,
            @Value("${DEMO_USER_EMAIL:}") String emailUsuario,
            @Value("${DEMO_USER_PASSWORD:}") String senhaUsuario,
            @Value("${DEMO_TECNICO_EMAIL:}") String emailTecnico,
            @Value("${DEMO_TECNICO_PASSWORD:}") String senhaTecnico) {
        return args -> {
            criarSeNaoExistir(repository, encoder, emailUsuario, senhaUsuario, "Usuário de teste", Role.USER);
            criarSeNaoExistir(repository, encoder, emailTecnico, senhaTecnico, "Técnico de teste", Role.MODERATOR);
        };
    }

    private void criarSeNaoExistir(UsuarioRepository repository, PasswordEncoder encoder,
            String email, String senha, String nome, Role role) {
        if (email == null || senha == null || email.isBlank() || senha.isBlank()) return;
        if (senha.length() < 6) throw new IllegalArgumentException("Senha de demonstração deve ter ao menos 6 caracteres.");
        String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmail(emailNormalizado)) return;
        Usuario usuario = new Usuario(nome, emailNormalizado, encoder.encode(senha), role);
        repository.save(usuario);
    }
}
