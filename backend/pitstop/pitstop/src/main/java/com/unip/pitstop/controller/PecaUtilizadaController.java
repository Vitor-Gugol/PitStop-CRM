package com.unip.pitstop.controller;

import com.unip.pitstop.model.PecaUtilizada;
import com.unip.pitstop.repository.PecaUtilizadaRepository;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.PecaRepository;
import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.model.Peca;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/pecas-utilizadas")
public class PecaUtilizadaController {

    @Autowired
    private PecaUtilizadaRepository pecaUtilizadaRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private PecaRepository pecaRepository;

    // Listar todas as peças utilizadas
    @GetMapping
    public List<PecaUtilizada> listarTodas() {
        return pecaUtilizadaRepository.findAll();
    }

    // Adicionar uma peça a uma ordem de serviço
    @PostMapping
    public PecaUtilizada adicionar(@Valid @RequestBody PecaUtilizada pecaUtilizada) {
        // Verificar se a OS existe
        OrdemServico ordemServico = ordemServicoRepository.findById(pecaUtilizada.getOrdemServico().getIdOs())
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        // Verificar se a peça existe
        Peca peca = pecaRepository.findById(pecaUtilizada.getPeca().getIdPeca())
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));

        // Verificar se a peça já está associada à OS
        Optional<PecaUtilizada> existente = pecaUtilizadaRepository
                .findByOrdemServicoAndPeca(ordemServico, peca);
        if (existente.isPresent()) {
            throw new RuntimeException("Peça já está associada à Ordem de Serviço.");
        }


        // Atualizar estoque
        if (peca.getQuantidadeEstoque() < pecaUtilizada.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para a peça " + peca.getNome());
        }
        peca.setQuantidadeEstoque(peca.getQuantidadeEstoque() - pecaUtilizada.getQuantidade());
        pecaRepository.save(peca);

        // Configurar e salvar
        pecaUtilizada.setOrdemServico(ordemServico);
        pecaUtilizada.setPeca(peca);
        pecaUtilizada.setPrecoUnitario(peca.getPreco());
        return pecaUtilizadaRepository.save(pecaUtilizada);
    }

}
