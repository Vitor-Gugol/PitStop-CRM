package com.unip.pitstop.dto;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
public class OrdemDetalhesPecaServicoDTO {
    private Long idOs;
    private List<PecaUtilizadaDTO> pecasUtilizadas;
    private List<ServicoRealizadoDTO> servicosRealizados;

    // Construtor
    public OrdemDetalhesPecaServicoDTO(Long idOs, List<PecaUtilizadaDTO> pecasUtilizadas, List<ServicoRealizadoDTO> servicosRealizados) {
        this.idOs = idOs;
        this.pecasUtilizadas = pecasUtilizadas;
        this.servicosRealizados = servicosRealizados;
    }

    // Getters e Setters
    public Long getIdOs() {
        return idOs;
    }

    public void setIdOs(Long idOs) {
        this.idOs = idOs;
    }

    public List<PecaUtilizadaDTO> getPecasUtilizadas() {
        return pecasUtilizadas;
    }

    public void setPecasUtilizadas(List<PecaUtilizadaDTO> pecasUtilizadas) {
        this.pecasUtilizadas = pecasUtilizadas;
    }

    public List<ServicoRealizadoDTO> getServicosRealizados() {
        return servicosRealizados;
    }

    public void setServicosRealizados(List<ServicoRealizadoDTO> servicosRealizados) {
        this.servicosRealizados = servicosRealizados;
    }
}
