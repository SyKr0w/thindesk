package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.Role;
import com.pfc.thindesk.entity.StatusChamado;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.model.NovoChamado;
import com.pfc.thindesk.model.SolucaoChamado;
import com.pfc.thindesk.repository.ChamadoRepository;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChamadoServiceTest {

    @Mock ChamadoRepository chamados;
    @Mock UsuarioRepository usuarios;
    private ChamadoService service;
    private Usuario comum;
    private Usuario tecnico;

    @BeforeEach
    void preparar() {
        service = new ChamadoService(chamados, usuarios);
        comum = new Usuario("Cliente", "cliente@teste.com", "hash", Role.USER);
        comum.setId("id-cliente");
        tecnico = new Usuario("Técnico", "tecnico@teste.com", "hash", Role.MODERATOR);
        tecnico.setId("id-tecnico");
    }

    @Test
    void abrirRegistraSolicitanteDataEStatus() {
        NovoChamado dados = new NovoChamado();
        dados.setTitulo(" Sem imagem ");
        dados.setDescricao(" Monitor apagado ");
        when(chamados.save(any(Chamado.class))).thenAnswer(inv -> inv.getArgument(0));
        Chamado salvo = service.abrir(comum, dados);
        assertEquals("Sem imagem", salvo.getTitulo());
        assertEquals("id-cliente", salvo.getSolicitanteId());
        assertNotNull(salvo.getDataAbertura());
        assertEquals(StatusChamado.ABERTO, salvo.getStatus());
    }

    @Test
    void usuarioNaoPodeVerChamadoDeOutraPessoa() {
        Chamado chamado = new Chamado();
        chamado.setSolicitanteId("outro-id");
        when(chamados.findById("id-1")).thenReturn(Optional.of(chamado));
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.buscar("id-1", comum));
        assertEquals(HttpStatus.FORBIDDEN, erro.getStatusCode());
    }

    @Test
    void tecnicoPodeResolver() {
        Chamado chamado = new Chamado();
        chamado.setStatus(StatusChamado.ABERTO);
        when(chamados.findById("id-2")).thenReturn(Optional.of(chamado));
        when(chamados.save(any(Chamado.class))).thenAnswer(inv -> inv.getArgument(0));
        SolucaoChamado dados = new SolucaoChamado();
        dados.setSolucao("Substituição do cabo");
        Chamado salvo = service.resolver("id-2", tecnico, dados);
        assertEquals(StatusChamado.RESOLVIDO, salvo.getStatus());
        assertEquals("id-tecnico", salvo.getTecnicoId());
        assertNotNull(salvo.getDataResolucao());
    }

    @Test
    void naoPermiteResolverDuasVezes() {
        Chamado chamado = new Chamado();
        chamado.setStatus(StatusChamado.RESOLVIDO);
        when(chamados.findById("id-3")).thenReturn(Optional.of(chamado));
        SolucaoChamado dados = new SolucaoChamado();
        dados.setSolucao("Nova solução");
        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> service.resolver("id-3", tecnico, dados));
        assertEquals(HttpStatus.CONFLICT, erro.getStatusCode());
        verify(chamados, never()).save(any(Chamado.class));
    }
}
