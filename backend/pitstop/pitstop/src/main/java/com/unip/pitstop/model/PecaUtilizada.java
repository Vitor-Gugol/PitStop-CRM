package com.unip.pitstop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "peca_utilizada")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PecaUtilizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPecaUtilizada;

    @Column(name = "id_peca", nullable = false)
    private Long idPeca;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_os", nullable = false)
    private OrdemServico ordemServico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_peca", insertable = false, updatable = false)
    private Peca peca;


    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
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

    public Long getIdPeca() {
        return idPeca;
    }

    public void setIdPeca(Long idPeca) {
        this.idPeca = idPeca;
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
