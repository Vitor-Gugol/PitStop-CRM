package com.unip.pitstop.dto;

public class ServicoRealizadoDetalheDTO {
    private Long idServicoRealizado;
    private Long idServico;
    private String nomeServico;
    private Double precoCobrado;

    public ServicoRealizadoDetalheDTO(Long idServicoRealizado, Long idServico, String nomeServico, Double precoCobrado) {
        this.idServicoRealizado = idServicoRealizado;
        this.idServico = idServico;
        this.nomeServico = nomeServico;
        this.precoCobrado = precoCobrado;
    }

    // Getters e Setters
    public Long getIdServicoRealizado() {
        return idServicoRealizado;
    }

    public void setIdServicoRealizado(Long idServicoRealizado) {
        this.idServicoRealizado = idServicoRealizado;
    }

    public Long getIdServico() {
        return idServico;
    }

    public void setIdServico(Long idServico) {
        this.idServico = idServico;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public Double getPrecoCobrado() {
        return precoCobrado;
    }

    public void setPrecoCobrado(Double precoCobrado) {
        this.precoCobrado = precoCobrado;
    }
}