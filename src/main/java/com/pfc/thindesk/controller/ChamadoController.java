package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.model.NovoChamado;
import com.pfc.thindesk.model.SolucaoChamado;
import com.pfc.thindesk.service.ChamadoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService service;

    public ChamadoController(ChamadoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chamado abrir(Authentication auth, @Valid @RequestBody NovoChamado dados) {
        return service.abrir(service.usuarioAtual(auth.getName()), dados);
    }

    @GetMapping("/usuario/{id}")
    public List<Chamado> listarPorUsuario(@PathVariable String id, Authentication auth) {
        return service.listarPorUsuario(id, service.usuarioAtual(auth.getName()));
    }

    @GetMapping("/{id}")
    public Chamado consultar(@PathVariable String id, Authentication auth) {
        return service.buscar(id, service.usuarioAtual(auth.getName()));
    }

    @PutMapping("/{id}/resolver")
    public Chamado resolver(@PathVariable String id, Authentication auth,
                          @Valid @RequestBody SolucaoChamado dados) {
        return service.resolver(id, service.usuarioAtual(auth.getName()), dados);
    }
}
