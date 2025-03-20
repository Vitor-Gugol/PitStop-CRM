package com.unip.pitstop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordem_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOs;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "O cliente é obrigatório.")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_carro", nullable = false)
    @NotNull(message = "O carro é obrigatório.")
    private Carro carro;

    private LocalDateTime dataEntrada = LocalDateTime.now();
    private LocalDateTime dataPrevistaSaida;
    private String status; // Ex.: "Em andamento", "Concluído", "Cancelado"
    private Double valorTotal;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PecaUtilizada> pecasUtilizadas;


    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicoRealizado> servicosRealizados;


    // Getters e Setters

    public Long getIdOs() {
        return idOs;
    }

    public void setIdOs(Long idOs) {
        this.idOs = idOs;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataPrevistaSaida() {
        return dataPrevistaSaida;
    }

    public void setDataPrevistaSaida(LocalDateTime dataPrevistaSaida) {
        this.dataPrevistaSaida = dataPrevistaSaida;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<PecaUtilizada> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public void setPecasUtilizadas(List<PecaUtilizada> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas;
    }

    public List<ServicoRealizado> getServicosRealizados() {
        return servicosRealizados;
    }

    public void setServicosRealizados(List<ServicoRealizado> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }
}
