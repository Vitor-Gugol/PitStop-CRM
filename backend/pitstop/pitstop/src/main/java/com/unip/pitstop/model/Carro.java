package com.unip.pitstop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "carro")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarro;

    @NotNull(message = "O cliente associado ao carro é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY otimiza o carregamento
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotBlank(message = "A marca do carro é obrigatória.")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "O modelo do carro é obrigatório.")
    @Column(nullable = false)
    private String modelo;

    @Positive(message = "O ano deve ser maior que zero.")
    @Column(nullable = false)
    private int ano;

    @NotBlank(message = "A placa do carro é obrigatória.")
    @Column(nullable = false, unique = true)
    private String placa;

    @NotBlank(message = "O chassi do carro é obrigatório.")
    @Column(nullable = false, unique = true)
    private String chassi;

    private String cor;

    // Getters e Setters

    public Long getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(Long idCarro) {
        this.idCarro = idCarro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
