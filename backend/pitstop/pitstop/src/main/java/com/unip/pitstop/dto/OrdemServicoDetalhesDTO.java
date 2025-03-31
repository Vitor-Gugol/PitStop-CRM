package com.unip.pitstop.dto;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

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
    private List<ServicoRealizadoDetalheDTO> servicosRealizados; // Use o novo DTO
    private List<PecaUtilizadaDetalheDTO> pecasUtilizadas; // Use o novo DTO


    public OrdemServicoDetalhesDTO(Long idOs, String clienteNome, String carroModelo, String carroPlaca, String dataEntrada, String dataPrevistaSaida, String valorTotal, String status, List<ServicoRealizadoDetalheDTO> servicosRealizados, List<PecaUtilizadaDetalheDTO> pecasUtilizadas) {
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

    // Getters e Setters (atualize os tipos das listas)
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

    public List<ServicoRealizadoDetalheDTO> getServicosRealizados() {
        return servicosRealizados;
    }

    public void setServicosRealizados(List<ServicoRealizadoDetalheDTO> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }

    public List<PecaUtilizadaDetalheDTO> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public void setPecasUtilizadas(List<PecaUtilizadaDetalheDTO> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas;
    }
}