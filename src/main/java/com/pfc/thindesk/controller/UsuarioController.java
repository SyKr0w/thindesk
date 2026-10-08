/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author alunocmc
 */

@Controller
public class UsuarioController {
    private final UsuarioService usuarioService;
 
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
 
    @GetMapping("/cadastro")
    public String mostrarCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }
 
    @PostMapping("/cadastro")
    public String cadastrar(
            @ModelAttribute Usuario usuario,
            Model model) {
 
        try {
            usuarioService.cadastrar(usuario);
 
            return "redirect:/login?cadastroSucesso";
        } catch (IllegalArgumentException e) {
 
            model.addAttribute("erro", e.getMessage());
 
            return "cadastro";
        }
    }
}