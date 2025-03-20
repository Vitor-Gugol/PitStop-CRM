package com.unip.pitstop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servico_realizado")
public class ServicoRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicoRealizado;

    @ManyToOne
    @JoinColumn(name = "id_os", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;

    @Column(nullable = false)
    private Double precoCobrado;

    // Getters e Setters

    public Long getIdServicoRealizado() {
        return idServicoRealizado;
    }

    public void setIdServicoRealizado(Long idServicoRealizado) {
        this.idServicoRealizado = idServicoRealizado;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Double getPrecoCobrado() {
        return precoCobrado;
    }

    public void setPrecoCobrado(Double precoCobrado) {
        this.precoCobrado = precoCobrado;
    }
}
