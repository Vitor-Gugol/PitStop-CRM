package com.unip.pitstop.controller;

import com.unip.pitstop.model.Cliente;
import com.unip.pitstop.repository.ClienteRepository;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    // Listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        try {
            logger.info("Buscando todos os clientes...");
            List<Cliente> clientes = clienteRepository.findAll();
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            logger.error("Erro ao listar os clientes: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Cadastrar um novo cliente
    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Cliente cliente) {
        try {
            logger.info("Cadastrando novo cliente: {}", cliente.getNome());
            Cliente clienteSalvo = clienteRepository.save(cliente);
            logger.info("Cliente cadastrado com sucesso: {}", clienteSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (Exception e) {
            logger.error("Erro ao cadastrar cliente: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar cliente.");
        }
    }

    // Atualizar um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            logger.info("Atualizando cliente com ID: {}", id);
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado: ID " + id));

            // Atualizando campos
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setTelefone(clienteAtualizado.getTelefone());
            cliente.setEmail(clienteAtualizado.getEmail());
            cliente.setEndereco(clienteAtualizado.getEndereco());

            Cliente clienteSalvo = clienteRepository.save(cliente);
            logger.info("Cliente atualizado com sucesso: {}", clienteSalvo);
            return ResponseEntity.ok(clienteSalvo);
        } catch (RuntimeException e) {
            logger.error("Erro de validação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao atualizar cliente: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar cliente.");
        }
    }

    // Excluir um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            logger.info("Excluindo cliente com ID: {}", id);
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado: ID " + id));
            clienteRepository.delete(cliente);
            logger.info("Cliente excluído com sucesso: ID {}", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Erro de validação: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro ao excluir cliente: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao excluir cliente.");
        }
    }
}
