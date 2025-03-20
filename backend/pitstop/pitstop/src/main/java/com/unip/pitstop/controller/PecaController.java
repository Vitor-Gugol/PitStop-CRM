package com.unip.pitstop.controller;

import com.unip.pitstop.model.Peca;
import com.unip.pitstop.repository.PecaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;

    // Listar todas as peças
    @GetMapping
    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }

    // Cadastrar uma nova peça
    @PostMapping
    public Peca cadastrar(@Valid @RequestBody Peca peca) {
        return pecaRepository.save(peca);
    }

    // Atualizar uma peça existente
    @PutMapping("/{id}")
    public Peca atualizar(@PathVariable Long id, @RequestBody Peca pecaAtualizada) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
        peca.setNome(pecaAtualizada.getNome());
        peca.setDescricao(pecaAtualizada.getDescricao());
        peca.setPreco(pecaAtualizada.getPreco());
        peca.setQuantidadeEstoque(pecaAtualizada.getQuantidadeEstoque());
        return pecaRepository.save(peca);
    }

    // Excluir uma peça
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        Peca peca = pecaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
        pecaRepository.delete(peca);
    }
}
