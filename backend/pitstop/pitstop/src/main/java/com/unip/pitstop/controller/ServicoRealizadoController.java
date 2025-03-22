package com.unip.pitstop.controller;

import com.unip.pitstop.model.ServicoRealizado;
import com.unip.pitstop.repository.ServicoRealizadoRepository;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.ServicoRepository;
import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.model.Servico;
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
@RequestMapping("/servicos-realizados")
public class ServicoRealizadoController {

    @Autowired
    private ServicoRealizadoRepository servicoRealizadoRepository;

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ServicoRealizadoController.class);

    // Listar todos os serviços realizados
    @GetMapping
    public ResponseEntity<List<ServicoRealizado>> listarTodos() {
        try {
            logger.info("Buscando todos os serviços realizados...");
            List<ServicoRealizado> servicosRealizados = servicoRealizadoRepository.findAll();
            return ResponseEntity.ok(servicosRealizados);
        } catch (Exception e) {
            logger.error("Erro ao buscar serviços realizados: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Adicionar um serviço a uma ordem de serviço
    @PostMapping
    public ResponseEntity<?> adicionar(@Valid @RequestBody ServicoRealizado servicoRealizado) {
        try {
            // Verificar se a Ordem de Serviço existe
            OrdemServico ordemServico = ordemServicoRepository.findById(servicoRealizado.getOrdemServico().getIdOs())
                    .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada: ID " + servicoRealizado.getOrdemServico().getIdOs()));

            // Verificar se o serviço existe
            Servico servico = servicoRepository.findById(servicoRealizado.getServico().getIdServico())
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado: ID " + servicoRealizado.getServico().getIdServico()));

            // Configurar o serviço realizado
            servicoRealizado.setOrdemServico(ordemServico);
            servicoRealizado.setServico(servico);
            servicoRealizado.setPrecoCobrado(servico.getPreco());

            // Salvar no repositório
            ServicoRealizado servicoSalvo = servicoRealizadoRepository.save(servicoRealizado);
            logger.info("Serviço realizado salvo com sucesso: {}", servicoSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(servicoSalvo);

        } catch (RuntimeException e) {
            logger.error("Erro de validação: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao adicionar serviço realizado: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar o serviço realizado: " + e.getMessage());
        }
    }
}
