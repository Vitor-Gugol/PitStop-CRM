package com.unip.pitstop.dto;

import java.time.LocalDateTime;

public class FluxoCarrosDTO {
    private Long idOs;
    private String placa;
    private String modelo;
    private String clienteNome;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida; // Pode ser null se o carro não saiu
    private String status;

    public FluxoCarrosDTO(Long idOs, String placa, String modelo, String clienteNome, LocalDateTime dataEntrada, LocalDateTime dataSaida, String status) {
        this.idOs = idOs;
        this.placa = placa;
        this.modelo = modelo;
        this.clienteNome = clienteNome;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = status;
    }

    // Getters
    public Long getIdOs() {
        return idOs;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public String getStatus() {
        return status;
    }
}