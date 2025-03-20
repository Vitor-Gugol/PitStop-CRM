package com.unip.pitstop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "peca_utilizada")
public class PecaUtilizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPecaUtilizada;


    @ManyToOne
    @JoinColumn(name = "id_os", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "id_peca", nullable = false)
    private Peca peca;

    @Column(nullable = false)
    @NotNull(message = "O campo quantidade não pode ser nulo.")
    private Integer quantidade;

    @Column(nullable = false)
    @NotNull(message = "O campo preço unitário não pode ser nulo.")
    private Double precoUnitario;

    // Getters e Setters

    public Long getIdPecaUtilizada() {
        return idPecaUtilizada;
    }

    public void setIdPecaUtilizada(Long idPecaUtilizada) {
        this.idPecaUtilizada = idPecaUtilizada;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
