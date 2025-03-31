package com.unip.pitstop.dto;

public class PecaUtilizadaDetalheDTO {
    private Long idPecaUtilizada;
    private Long idPeca;
    private String nomePeca;
    private Integer quantidade;
    private Double precoUnitario;

    public PecaUtilizadaDetalheDTO(Long idPecaUtilizada, Long idPeca, String nomePeca, Integer quantidade, Double precoUnitario) {
        this.idPecaUtilizada = idPecaUtilizada;
        this.idPeca = idPeca;
        this.nomePeca = nomePeca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public Long getIdPecaUtilizada() {
        return idPecaUtilizada;
    }

    public void setIdPecaUtilizada(Long idPecaUtilizada) {
        this.idPecaUtilizada = idPecaUtilizada;
    }

    public Long getIdPeca() {
        return idPeca;
    }

    public void setIdPeca(Long idPeca) {
        this.idPeca = idPeca;
    }

    public String getNomePeca() {
        return nomePeca;
    }

    public void setNomePeca(String nomePeca) {
        this.nomePeca = nomePeca;
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