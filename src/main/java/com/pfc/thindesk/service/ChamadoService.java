package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.Role;
import com.pfc.thindesk.entity.StatusChamado;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.model.NovoChamado;
import com.pfc.thindesk.model.SolucaoChamado;
import com.pfc.thindesk.repository.ChamadoRepository;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario usuarioAtual(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Conta não encontrada."));
    }

    public boolean ehTecnico(Usuario usuario) {
        return usuario.getRole() == Role.MODERATOR || usuario.getRole() == Role.ADMIN;
    }

    public Chamado abrir(Usuario solicitante, NovoChamado dados) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dados.getTitulo().trim());
        chamado.setDescricao(dados.getDescricao().trim());
        chamado.setSolicitanteId(solicitante.getId());
        chamado.setSolicitanteNome(solicitante.getNome());
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setStatus(StatusChamado.ABERTO);
        return repository.save(chamado);
    }

    public List<Chamado> listar(Usuario usuario) {
        return ehTecnico(usuario)
                ? repository.findAllByOrderByDataAberturaDesc()
                : repository.findBySolicitanteIdOrderByDataAberturaDesc(usuario.getId());
    }

    public List<Chamado> listarPorUsuario(String usuarioId, Usuario atual) {
        if (!ehTecnico(atual) && !atual.getId().equals(usuarioId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para consultar este usuário.");
        }
        return repository.findBySolicitanteIdOrderByDataAberturaDesc(usuarioId);
    }

    public Chamado buscar(String id, Usuario atual) {
        Chamado chamado = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado não encontrado."));
        if (!ehTecnico(atual) && !atual.getId().equals(chamado.getSolicitanteId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão para consultar este chamado.");
        }
        return chamado;
    }

    public Chamado resolver(String id, Usuario tecnico, SolucaoChamado dados) {
        if (!ehTecnico(tecnico)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente técnicos podem resolver chamados.");
        }
        Chamado chamado = buscar(id, tecnico);
        if (chamado.getStatus() == StatusChamado.RESOLVIDO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Chamado já resolvido.");
        }
        chamado.setSolucao(dados.getSolucao().trim());
        chamado.setTecnicoId(tecnico.getId());
        chamado.setTecnicoNome(tecnico.getNome());
        chamado.setStatus(StatusChamado.RESOLVIDO);
        chamado.setDataResolucao(LocalDateTime.now());
        return repository.save(chamado);
    }
}
