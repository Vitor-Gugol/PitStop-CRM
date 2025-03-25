package com.unip.pitstop.dto;

import com.unip.pitstop.model.PecaUtilizada;
import com.unip.pitstop.model.ServicoRealizado;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
public class OrdemServicoDetalhesDTO {
    private Long idOs;
    private String clienteNome;
    private String carroModelo;
    private String carroPlaca;
    private String dataEntrada;
    private String dataPrevistaSaida;
    private String valorTotal;
    private String status;
    private List<ServicoRealizado> servicosRealizados;
    private List<PecaUtilizada> pecasUtilizadas;

    // Construtor
    public OrdemServicoDetalhesDTO(Long idOs, String clienteNome, String carroModelo, String carroPlaca, String dataEntrada, String dataPrevistaSaida, String valorTotal, String status, List<ServicoRealizado> servicosRealizados, List<PecaUtilizada> pecasUtilizadas) {
        this.idOs = idOs;
        this.clienteNome = clienteNome;
        this.carroModelo = carroModelo;
        this.carroPlaca = carroPlaca;
        this.dataEntrada = dataEntrada;
        this.dataPrevistaSaida = dataPrevistaSaida;
        this.valorTotal = valorTotal;
        this.status = status;
        this.servicosRealizados = servicosRealizados;
        this.pecasUtilizadas = pecasUtilizadas;
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

    public String getCarroPlaca() {
        return carroPlaca;
    }

    public void setCarroPlaca(String carroPlaca) {
        this.carroPlaca = carroPlaca;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDataPrevistaSaida() {
        return dataPrevistaSaida;
    }

    public void setDataPrevistaSaida(String dataPrevistaSaida) {
        this.dataPrevistaSaida = dataPrevistaSaida;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ServicoRealizado> getServicosRealizados() {
        return servicosRealizados;
    }

    public void setServicosRealizados(List<ServicoRealizado> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }

    public List<PecaUtilizada> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public void setPecasUtilizadas(List<PecaUtilizada> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas;
    }
}
