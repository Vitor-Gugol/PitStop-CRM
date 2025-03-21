package com.unip.pitstop.controller;

import com.unip.pitstop.model.Servico;
import com.unip.pitstop.repository.ServicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    // Listar todos os serviços
    @GetMapping
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    // Cadastrar um novo serviço
    @PostMapping
    public Servico cadastrar(@Valid @RequestBody Servico servico) {
        return servicoRepository.save(servico);
    }

    // Atualizar um serviço existente
    @PutMapping("/{id}")
    public Servico atualizar(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        servico.setDescricao(servicoAtualizado.getDescricao());
        servico.setPreco(servicoAtualizado.getPreco());
        return servicoRepository.save(servico);
    }

    // Excluir um serviço
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        servicoRepository.delete(servico);
    }
}
