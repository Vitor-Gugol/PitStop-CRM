package com.unip.pitstop.dto;


import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
public class ServicoRealizadoDTO {
    private String nome; // Nome do serviço
    private double precoCobrado; // Preço cobrado pelo serviço

    // Construtor
    public ServicoRealizadoDTO(String nome, double precoCobrado) {
        this.nome = nome;
        this.precoCobrado = precoCobrado;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrecoCobrado() {
        return precoCobrado;
    }

    public void setPrecoCobrado(double precoCobrado) {
        this.precoCobrado = precoCobrado;
    }
}
