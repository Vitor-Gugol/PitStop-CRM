package com.unip.pitstop.controller;

import com.unip.pitstop.model.ServicoRealizado;
import com.unip.pitstop.repository.ServicoRealizadoRepository;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.ServicoRepository;
import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.model.Servico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos-realizados")
public class ServicoRealizadoController {

    @Autowired
    private ServicoRealizadoRepository servicoRealizadoRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    // Listar todos os serviços realizados
    @GetMapping
    public List<ServicoRealizado> listarTodos() {
        return servicoRealizadoRepository.findAll();
    }

    // Adicionar um serviço a uma ordem de serviço
    @PostMapping
    public ServicoRealizado adicionar(@Valid @RequestBody ServicoRealizado servicoRealizado) {
        // Verificar se a OS existe
        OrdemServico ordemServico = ordemServicoRepository.findById(servicoRealizado.getOrdemServico().getIdOs())
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        // Verificar se o serviço existe
        Servico servico = servicoRepository.findById(servicoRealizado.getServico().getIdServico())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Configurar e salvar
        servicoRealizado.setOrdemServico(ordemServico);
        servicoRealizado.setServico(servico);
        servicoRealizado.setPrecoCobrado(servico.getPreco());
        return servicoRealizadoRepository.save(servicoRealizado);
    }
}
