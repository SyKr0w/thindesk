package com.pfc.thindesk.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SolucaoChamado {

    @NotBlank(message = "A solução é obrigatória.")
    @Size(max = 2000, message = "A solução deve ter no máximo 2000 caracteres.")
    private String solucao;

    public String getSolucao() { return solucao; }
    public void setSolucao(String solucao) { this.solucao = solucao; }
}
