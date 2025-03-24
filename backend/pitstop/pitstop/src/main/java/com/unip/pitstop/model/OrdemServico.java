package com.unip.pitstop.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOs;

    // A data de entrada é definida automaticamente como o momento atual
    private LocalDateTime dataEntrada = LocalDateTime.now();

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Gerencia referência para evitar problemas de serialização
    private List<ServicoRealizado> servicosRealizados;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // Gerencia referência para evitar problemas de serialização
    private List<PecaUtilizada> pecasUtilizadas;

    @NotNull(message = "O cliente é obrigatório.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "O carro é obrigatório.")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_carro", nullable = false)
    private Carro carro;

    @NotNull(message = "A data prevista de saída é obrigatória.")
    private LocalDateTime dataPrevistaSaida;

    @NotNull(message = "O status é obrigatório.")
    @Pattern(regexp = "Em andamento|Cancelado|Concluído", message = "Status inválido.")
    private String status;

    private Double valorTotal;

    // Getters e Setters

    public Long getIdOs() {
        return idOs;
    }

    public void setIdOs(Long idOs) {
        this.idOs = idOs;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
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
}
