package com.unip.pitstop.controller;

import com.unip.pitstop.dto.OrdemServicoDTO;
import com.unip.pitstop.model.*;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.ClienteRepository;
import com.unip.pitstop.repository.CarroRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarroRepository carroRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoController.class);

    // Listar ordens com paginação completa
    @GetMapping("/completo/paginado")
    public Page<OrdemServico> listarPaginado(Pageable pageable) {
        logger.info("Buscando ordens de serviço paginadas...");
        return ordemServicoRepository.findAll(pageable);
    }

    // Listar dados do dashboard com DTO e paginação
    @GetMapping("/dashboard")
    public Page<OrdemServicoDTO> listarParaPaginaDashboard(Pageable pageable) {
        logger.info("Buscando dados do dashboard...");
        return ordemServicoRepository.findAll(pageable)
                .map(ordem -> new OrdemServicoDTO(
                        ordem.getIdOs(),
                        ordem.getCliente() != null ? ordem.getCliente().getNome() : "Cliente não informado",
                        ordem.getCarro() != null ? ordem.getCarro().getModelo() : "Carro não informado",
                        ordem.getDataEntrada() != null ? ordem.getDataEntrada().toString() : "Data não informada",
                        ordem.getStatus()
                ));
    }

    // Cadastrar nova ordem de serviço
    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@Valid @RequestBody OrdemServico ordemServico, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            logger.error("Erros de validação: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Garantir que cliente e carro não sejam nulos
            if (ordemServico.getCliente() == null || ordemServico.getCarro() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente ou carro não informado");
            }

            // Buscar ou criar cliente
            Cliente cliente = clienteRepository.findByNome(ordemServico.getCliente().getNome())
                    .orElseGet(() -> clienteRepository.save(ordemServico.getCliente()));

            // Buscar ou criar carro
            Carro carro = carroRepository.findByPlaca(ordemServico.getCarro().getPlaca())
                    .orElseGet(() -> carroRepository.save(ordemServico.getCarro()));

            // Associar cliente e carro à ordem
            ordemServico.setCliente(cliente);
            ordemServico.setCarro(carro);

            // Validar e associar peças e serviços
            if (ordemServico.getPecasUtilizadas() != null) {
                for (PecaUtilizada peca : ordemServico.getPecasUtilizadas()) {
                    if (peca.getPrecoUnitario() == null || peca.getPrecoUnitario() <= 0) {
                        throw new RuntimeException("Preço unitário inválido para a peça de ID: " + peca.getIdPecaUtilizada());
                    }
                    peca.setOrdemServico(ordemServico);
                }
            }

            if (ordemServico.getServicosRealizados() != null) {
                for (ServicoRealizado servico : ordemServico.getServicosRealizados()) {
                    servico.setOrdemServico(ordemServico);
                }
            }

            // Salvar ordem de serviço
            OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
            logger.info("Ordem de serviço salva com sucesso: {}", ordemSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(ordemSalva);

        } catch (Exception e) {
            logger.error("Erro ao salvar a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a ordem de serviço: " + e.getMessage());
        }
    }

    // Atualizar uma ordem de serviço existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemAtualizada) {
        try {
            OrdemServico ordem = ordemServicoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

            ordem.setDataPrevistaSaida(ordemAtualizada.getDataPrevistaSaida());
            ordem.setStatus(ordemAtualizada.getStatus());
            ordem.setValorTotal(ordemAtualizada.getValorTotal());

            OrdemServico ordemSalva = ordemServicoRepository.save(ordem);
            logger.info("Ordem de serviço atualizada com sucesso: {}", ordemSalva);
            return ResponseEntity.ok(ordemSalva);

        } catch (Exception e) {
            logger.error("Erro ao atualizar a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a ordem de serviço: " + e.getMessage());
        }
    }

    // Excluir uma ordem de serviço
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            OrdemServico ordem = ordemServicoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

            ordemServicoRepository.delete(ordem);
            logger.info("Ordem de serviço excluída com sucesso: ID {}", id);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            logger.error("Erro ao excluir a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir a ordem de serviço: " + e.getMessage());
        }
    }
}
