package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.StatusChamado;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.model.NovoChamado;
import com.pfc.thindesk.model.SolucaoChamado;
import com.pfc.thindesk.service.ChamadoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final ChamadoService service;

    public HomeController(ChamadoService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }

    @GetMapping("/")
    public String dashboard(Authentication auth, Model model,
                            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                            @RequestParam(name = "status", defaultValue = "TODOS") String status) {
        Usuario usuario = service.usuarioAtual(auth.getName());
        List<Chamado> chamados = service.listar(usuario);

        // Filtra pela data de abertura, incluindo o primeiro e o último dia.
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            model.addAttribute("erroFiltro", "A data inicial não pode ser posterior à data final.");
            chamados = List.of();
        } else if (dataInicio != null || dataFim != null) {
            chamados = chamados.stream()
                    .filter(c -> c.getDataAbertura() != null)
                    .filter(c -> dataInicio == null || !c.getDataAbertura().toLocalDate().isBefore(dataInicio))
                    .filter(c -> dataFim == null || !c.getDataAbertura().toLocalDate().isAfter(dataFim))
                    .toList();
        }

        StatusChamado filtroStatus = switch (status) {
            case "ABERTO" -> StatusChamado.ABERTO;
            case "RESOLVIDO" -> StatusChamado.RESOLVIDO;
            default -> null;
        };
        if (filtroStatus != null) {
            chamados = chamados.stream()
                    .filter(c -> c.getStatus() == filtroStatus)
                    .toList();
        }

        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        model.addAttribute("status", filtroStatus == null ? "TODOS" : filtroStatus.name());
        model.addAttribute("nome", usuario.getNome());
        model.addAttribute("total", chamados.size());
        model.addAttribute("abertos", chamados.stream().filter(c -> c.getStatus() == StatusChamado.ABERTO).count());
        model.addAttribute("resolvidos", chamados.stream().filter(c -> c.getStatus() == StatusChamado.RESOLVIDO).count());
        model.addAttribute("recentes", chamados.stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/chamados")
    public String listar(Authentication auth, Model model) {
        model.addAttribute("chamados", service.listar(service.usuarioAtual(auth.getName())));
        return "chamados";
    }

    @GetMapping("/chamados/novo")
    public String novo(Model model) {
        model.addAttribute("novoChamado", new NovoChamado());
        return "novo-chamado";
    }

    @PostMapping("/chamados/novo")
    public String abrir(Authentication auth,
                        @Valid @ModelAttribute("novoChamado") NovoChamado dados,
                        BindingResult result) {
        if (result.hasErrors()) return "novo-chamado";
        Chamado salvo = service.abrir(service.usuarioAtual(auth.getName()), dados);
        return "redirect:/chamados/" + salvo.getId() + "?criado";
    }

    @GetMapping("/chamados/{id}")
    public String detalhes(@PathVariable String id, Authentication auth, Model model) {
        model.addAttribute("chamado", service.buscar(id, service.usuarioAtual(auth.getName())));
        model.addAttribute("solucaoChamado", new SolucaoChamado());
        return "detalhe-chamado";
    }

    @PostMapping("/chamados/{id}/resolver")
    public String resolver(@PathVariable String id, Authentication auth,
                           @Valid @ModelAttribute("solucaoChamado") SolucaoChamado dados,
                           BindingResult result, Model model) {
        Usuario tecnico = service.usuarioAtual(auth.getName());
        if (result.hasErrors()) {
            model.addAttribute("chamado", service.buscar(id, tecnico));
            return "detalhe-chamado";
        }
        service.resolver(id, tecnico, dados);
        return "redirect:/chamados/" + id + "?resolvido";
    }
}
