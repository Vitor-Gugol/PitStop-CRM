package com.unip.pitstop.dto;


import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
public class OrdemServicoDTO {
    private Long idOs; // Identificador único da Ordem

    private String clienteNome;
    private String carroModelo;
    private String dataEntrada;
    private String status;


    // Construtor
    public OrdemServicoDTO(Long idOs, String clienteNome, String carroModelo, String dataEntrada, String status) {
        this.idOs = idOs;

        this.clienteNome = clienteNome;
        this.carroModelo = carroModelo;
        this.dataEntrada = dataEntrada;
        this.status = status;

    }

    // Getters e Setters
    public Long getIdOs() {
        return idOs;
    }

    public void setIdOs(Long idOs) {
        this.idOs = idOs;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getCarroModelo() {
        return carroModelo;
    }

    public void setCarroModelo(String carroModelo) {
        this.carroModelo = carroModelo;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
