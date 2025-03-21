package com.unip.pitstop.controller;

import com.unip.pitstop.model.ServicoRealizado;
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
    private final OrdemServicoRepository ordemServicoRepository;

    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private final CarroRepository carroRepository;


    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoController.class);

    public OrdemServicoController(OrdemServicoRepository ordemServicoRepository,
                                  ClienteRepository clienteRepository,
                                  CarroRepository carroRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.carroRepository = carroRepository;
    }

    @GetMapping("/completo/paginado")
    public Page<OrdemServico> listarPaginado(Pageable pageable) {
        return ordemServicoRepository.findAll(pageable);
    }



    @GetMapping("/dashboard")
    public Page<OrdemServicoDTO> listarParaPaginaDashboard(Pageable pageable) {
        return ordemServicoRepository.findAll(pageable)
                .map(ordem -> new OrdemServicoDTO(
                        ordem.getIdOs(),
                        ordem.getCliente().getNome(),
                        ordem.getCarro().getModelo(),
                        ordem.getDataEntrada().toString(),
                        ordem.getStatus()
                ));
    }



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
            // Verificar e salvar cliente
            Cliente cliente = clienteRepository.findByNome(ordemServico.getCliente().getNome())
                    .orElse(null);

            if (cliente == null) {
                cliente = ordemServico.getCliente();
                clienteRepository.save(cliente);
                logger.info("Cliente criado: {}", cliente);
            }

            // Verificar e salvar carro
            Carro carro = carroRepository.findByPlaca(ordemServico.getCarro().getPlaca())
                    .orElse(null);

            if (carro == null) {
                carro = ordemServico.getCarro();
                carroRepository.save(carro);
                logger.info("Carro criado: {}", carro);
            }

            // Associar cliente e carro à ordem de serviço
            ordemServico.setCliente(cliente);
            ordemServico.setCarro(carro);

            // Inicializar listas vazias se necessário
            if (ordemServico.getPecasUtilizadas() == null) {
                ordemServico.setPecasUtilizadas(new ArrayList<>());
            }
            if (ordemServico.getServicosRealizados() == null) {
                ordemServico.setServicosRealizados(new ArrayList<>());
            }

            // Associar ordem de serviço às peças e serviços
            for (PecaUtilizada peca : ordemServico.getPecasUtilizadas()) {
                peca.setOrdemServico(ordemServico);
            }
            for (ServicoRealizado servico : ordemServico.getServicosRealizados()) {
                servico.setOrdemServico(ordemServico);
            }

            // Calcular valor total
            Double valorTotal = 0.0;
            for (PecaUtilizada peca : ordemServico.getPecasUtilizadas()) {
                valorTotal += peca.getQuantidade() * peca.getPrecoUnitario();
            }
            for (ServicoRealizado servico : ordemServico.getServicosRealizados()) {
                valorTotal += servico.getPrecoCobrado();
            }
            ordemServico.setValorTotal(valorTotal);
            logger.info("Valor total da ordem calculado: {}", valorTotal);

            // Salvar a ordem de serviço
            OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
            logger.info("Ordem de serviço criada com sucesso: {}", ordemSalva);

            return ResponseEntity.status(HttpStatus.CREATED).body(ordemSalva);
        } catch (Exception e) {
            logger.error("Erro ao salvar a ordem de serviço", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a ordem: " + e.getMessage());
        }
    }




    // Atualizar uma ordem de serviço existente
    @PutMapping("/{id}")
    public OrdemServico atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemAtualizada) {
        OrdemServico ordem = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        ordem.setDataPrevistaSaida(ordemAtualizada.getDataPrevistaSaida());
        ordem.setStatus(ordemAtualizada.getStatus());
        ordem.setValorTotal(ordemAtualizada.getValorTotal());

        return ordemServicoRepository.save(ordem);
    }

    // Excluir uma ordem de serviço
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        OrdemServico ordem = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

        ordemServicoRepository.delete(ordem);
    }
}
