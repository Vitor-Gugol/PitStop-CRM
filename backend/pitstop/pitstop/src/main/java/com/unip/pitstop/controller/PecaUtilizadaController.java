package com.unip.pitstop.controller;

import com.unip.pitstop.model.PecaUtilizada;
import com.unip.pitstop.repository.PecaUtilizadaRepository;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.PecaRepository;
import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.model.Peca;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public PecaUtilizada adicionar(@RequestBody PecaUtilizada pecaUtilizada) {
        // Verificar se a OS existe
        OrdemServico ordemServico = ordemServicoRepository.findById(pecaUtilizada.getOrdemServico().getIdOs())
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        // Verificar se a peça existe
        Peca peca = pecaRepository.findById(pecaUtilizada.getPeca().getIdPeca())
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));

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
