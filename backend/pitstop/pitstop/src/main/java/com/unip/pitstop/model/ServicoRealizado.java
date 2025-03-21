package com.unip.pitstop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unip.pitstop.dto.OrdemServicoDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "servico_realizado")
public class ServicoRealizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicoRealizado;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_os", nullable = false)
    @JsonBackReference // Indica que este é o lado "controlado" da referência
    private OrdemServico ordemServico;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;


    @Column(nullable = false)
    private Double precoCobrado;

    // Getters e Setters

    public OrdemServicoDTO converterParaDTO(OrdemServico ordemServico) {
        return new OrdemServicoDTO(
                ordemServico.getIdOs(),
                ordemServico.getCliente().getNome(),
                ordemServico.getCarro().getModelo(),
                ordemServico.getDataEntrada().toString(),
                ordemServico.getStatus()
        );
    }

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
