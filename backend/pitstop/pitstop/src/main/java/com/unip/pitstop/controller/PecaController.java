package com.unip.pitstop.controller;

import com.unip.pitstop.model.Peca;
import com.unip.pitstop.repository.PecaRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/pecas")
public class PecaController {

    @Autowired
    private PecaRepository pecaRepository;

    private static final Logger logger = LoggerFactory.getLogger(PecaController.class);

    // Listar todas as peças
    @GetMapping
    public ResponseEntity<List<Peca>> listarTodas() {
        try {
            logger.info("Buscando todas as peças...");
            List<Peca> pecas = pecaRepository.findAll();
            return ResponseEntity.ok(pecas);
        } catch (Exception e) {
            logger.error("Erro ao listar as peças: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cadastrar uma nova peça
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Peca peca) {
        try {
            logger.info("Cadastrando nova peça: {}", peca.getNome());
            Peca pecaSalva = pecaRepository.save(peca);
            logger.info("Peça cadastrada com sucesso: {}", pecaSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(pecaSalva);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar a peça: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar a peça.");
        }
    }

    // Atualizar uma peça existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Peca pecaAtualizada) {
        try {
            logger.info("Atualizando a peça com ID: {}", id);
            Peca peca = pecaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Peça não encontrada"));

            // Atualizando campos
            peca.setNome(pecaAtualizada.getNome());
            peca.setDescricao(pecaAtualizada.getDescricao());
            peca.setPreco(pecaAtualizada.getPreco());
            peca.setQuantidadeEstoque(pecaAtualizada.getQuantidadeEstoque());

            Peca pecaSalva = pecaRepository.save(peca);
            logger.info("Peça atualizada com sucesso: {}", pecaSalva);
            return ResponseEntity.ok(pecaSalva);
        } catch (RuntimeException e) {
            logger.error("Erro de validação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar a peça: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar a peça.");
        }
    }

    // Excluir uma peça
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            logger.info("Excluindo a peça com ID: {}", id);
            Peca peca = pecaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Peça não encontrada"));
            pecaRepository.delete(peca);
            logger.info("Peça excluída com sucesso: {}", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Erro de validação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao excluir a peça: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir a peça.");
        }
    }
}
