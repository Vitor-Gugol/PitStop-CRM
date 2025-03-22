package com.unip.pitstop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServico;

    @NotBlank(message = "A descrição do serviço é obrigatória.")
    @Column(nullable = false)
    private String descricao;

    @NotNull(message = "O preço do serviço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(nullable = false)
    private Double preco;

    // Getters e Setters

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
