package com.unip.pitstop.controller;

import com.unip.pitstop.dto.*;
import com.unip.pitstop.model.*;
import com.unip.pitstop.repository.*;
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
import com.unip.pitstop.model.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.unip.pitstop.model.Peca;
import com.unip.pitstop.model.Servico;
import com.unip.pitstop.model.ServicoRealizado;

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

    @Autowired
    private PecaRepository pecaRepository;

    @Autowired
    private ServicoRepository servicoRepository;




    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoController.class);



//DTO para puxar dados dashboard inicial

    @GetMapping("/dashboard")
    public Page<OrdemServicoDTO> listarParaPaginaDashboard(Pageable pageable) {
        return ordemServicoRepository.findAllByOrderByDataEntradaDesc(pageable)
                .map(ordem -> new OrdemServicoDTO(
                        ordem.getIdOs(),
                        ordem.getCliente() != null ? ordem.getCliente().getNome() : "Cliente não informado",
                        ordem.getCarro() != null ? ordem.getCarro().getModelo() : "Carro não informado",
                        ordem.getDataEntrada() != null ? ordem.getDataEntrada().toString() : "Data não informada",
                        ordem.getStatus()
                ));
    }

    //DTO para puxar todos os dados para atualizar cada ordem de serviço
    @GetMapping("/detalhes/{id}")
    public OrdemServicoDetalhesDTO buscarDetalhesOrdem(@PathVariable Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

        return new OrdemServicoDetalhesDTO(
                ordemServico.getIdOs(),
                ordemServico.getCliente() != null ? ordemServico.getCliente().getNome() : "Cliente não informado",
                ordemServico.getCarro() != null ? ordemServico.getCarro().getModelo() : "Carro não informado",
                ordemServico.getCarro() != null ? ordemServico.getCarro().getPlaca() : "Placa não informada",
                ordemServico.getDataEntrada() != null ? ordemServico.getDataEntrada().toString() : "Data não informada",
                ordemServico.getDataPrevistaSaida() != null ? ordemServico.getDataPrevistaSaida().toString() : "Data não informada",
                ordemServico.getValorTotal() != null ? ordemServico.getValorTotal().toString() : "Valor não informado",
                ordemServico.getStatus(),
                ordemServico.getServicosRealizados(),
                ordemServico.getPecasUtilizadas()
        );
    }

    @GetMapping("/detalhes/pecas-servicos/{id}")
    public OrdemDetalhesPecaServicoDTO buscarDetalhesPecaServico(@PathVariable Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

        List<PecaUtilizadaDTO> pecasDTO = ordemServico.getPecasUtilizadas().stream()
                .map(pecaUtilizada -> new PecaUtilizadaDTO(
                        pecaUtilizada.getPeca().getNome(), // Aqui estamos buscando o nome da entidade Peça
                        pecaUtilizada.getQuantidade(),
                        pecaUtilizada.getPrecoUnitario()
                ))
                .collect(Collectors.toList());



        List<ServicoRealizadoDTO> servicosDTO = ordemServico.getServicosRealizados().stream()
                .map(servicoRealizado -> new ServicoRealizadoDTO(
                        servicoRealizado.getNome(), // Nome está na entidade Serviço
                        servicoRealizado.getPrecoCobrado()
                ))
                .collect(Collectors.toList());




        // Retornando o DTO principal
        return new OrdemDetalhesPecaServicoDTO(
                ordemServico.getIdOs(),
                pecasDTO,
                servicosDTO
        );
    }


    @GetMapping("/servicos/nomes")
    public List<String> listarNomesServicos() {
        return servicoRepository.findAll()
                .stream()
                .map(Servico::getDescricao) // Correct method for accessing the description
                .collect(Collectors.toList());
    }
    @GetMapping("/pecas")
    public List<Peca> listarTodas() {
        return pecaRepository.findAll();
    }
    @GetMapping("/servicos")
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
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

            if (ordemServico.getCliente() == null || ordemServico.getCliente().getEmail() == null) {
                throw new IllegalArgumentException("E-mail do cliente é obrigatório.");
            }


                    Cliente cliente = clienteRepository.findByEmail(ordemServico.getCliente().getEmail())
                            .orElseGet(() -> {
                                if (ordemServico.getCliente().getNome() == null) {
                                    throw new IllegalArgumentException("Nome do cliente é obrigatório ao criar um novo cliente.");
                                }
                                logger.info("Cliente não encontrado. Criando novo cliente: {}", ordemServico.getCliente());
                                return clienteRepository.save(ordemServico.getCliente());
                            });



            if (ordemServico.getCarro() == null || ordemServico.getCarro().getPlaca() == null) {
                throw new IllegalArgumentException("Placa do carro é obrigatória.");
            }

                            // Buscar carro pela placa ou criar um novo
                            Carro carro = carroRepository.findByPlaca(ordemServico.getCarro().getPlaca())
                                .orElseGet(() -> {
                                    if (ordemServico.getCarro().getMarca() == null || ordemServico.getCarro().getModelo() == null) {
                                        throw new IllegalArgumentException("Marca e modelo do carro são obrigatórios ao criar um novo carro.");
                                    }
                                    logger.info("Carro não encontrado. Criando novo carro: {}", ordemServico.getCarro());
                                    ordemServico.getCarro().setCliente(cliente); // Associar cliente ao carro
                                    return carroRepository.save(ordemServico.getCarro());
                                });


                        ordemServico.setCliente(cliente);
                        ordemServico.setCarro(carro);


                        ordemServicoRepository.save(ordemServico);

                        logger.info("Ordem de serviço salva com sucesso.");



            if (ordemServico.getPecasUtilizadas() != null) {
                for (PecaUtilizada pecaUtilizada : ordemServico.getPecasUtilizadas()) {
                    if (pecaUtilizada.getIdPeca() == null) { // Verificar ID da peça diretamente
                        throw new RuntimeException("O campo idPeca é obrigatório.");
                    }

                    // Buscar a peça pelo ID
                    Peca pecaExistente = pecaRepository.findById(pecaUtilizada.getIdPeca())
                            .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + pecaUtilizada.getIdPeca()));

                    // Associar a peça ao objeto PecaUtilizada
                    pecaUtilizada.setPeca(pecaExistente); // Relacionar objeto completo
                    pecaUtilizada.setOrdemServico(ordemServico);
                }
            }





            if (ordemServico.getServicosRealizados() != null) {
                for (ServicoRealizado servicoRealizado : ordemServico.getServicosRealizados()) {
                    if (servicoRealizado.getIdServico() == null) { // Verificar ID do serviço diretamente
                        throw new RuntimeException("O campo idServico é obrigatório.");
                    }

                    // Buscar o serviço pelo ID
                    Servico servicoExistente = servicoRepository.findById(servicoRealizado.getIdServico())
                            .orElseThrow(() -> new RuntimeException("Serviço não encontrado com ID: " + servicoRealizado.getIdServico()));

                    // Associar o serviço ao objeto ServicoRealizado
                    servicoRealizado.setServico(servicoExistente); // Relacionar objeto completo
                    servicoRealizado.setOrdemServico(ordemServico);
                }
            }




            // Salvar ordem de serviço
            OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
            logger.info("Ordem de serviço salva com sucesso: {}", ordemSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(ordemSalva);

        } catch (Exception e) {
            logger.error("Erro ao salvar a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao salvar a ordem de serviço: " + e.getMessage()));

        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemAtualizada) {
        try {
            OrdemServico ordemExistente = ordemServicoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada"));

            // Atualizar ou criar cliente
            if (ordemAtualizada.getCliente() != null && ordemAtualizada.getCliente().getIdCliente() != null) {
                logger.info("Cliente recebido no payload: {}", ordemAtualizada.getCliente());
                Cliente cliente = clienteRepository.findById(ordemAtualizada.getCliente().getIdCliente())
                        .orElseGet(() -> {
                            if (ordemAtualizada.getCliente().getNome() == null) {
                                throw new IllegalArgumentException("Nome do cliente é obrigatório ao criar um novo cliente.");
                            }
                            logger.info("Cliente não encontrado. Criando novo cliente: {}", ordemAtualizada.getCliente());
                            return clienteRepository.save(ordemAtualizada.getCliente());
                        });
                ordemExistente.setCliente(cliente); // Atualizar cliente na ordem
            }



            if (ordemAtualizada.getCarro() != null && ordemAtualizada.getCarro().getIdCarro() != null) {
                logger.info("Carro recebido no payload: {}", ordemAtualizada.getCarro());
                Carro carro = carroRepository.findById(ordemAtualizada.getCarro().getIdCarro())
                        .orElseGet(() -> {
                            if (ordemAtualizada.getCarro().getMarca() == null || ordemAtualizada.getCarro().getModelo() == null) {
                                throw new IllegalArgumentException("Marca e modelo do carro são obrigatórios ao criar um novo carro.");
                            }
                            logger.info("Carro não encontrado. Criando novo carro: {}", ordemAtualizada.getCarro());
                            return carroRepository.save(ordemAtualizada.getCarro());
                        });
                ordemExistente.setCarro(carro); // Atualizar carro na ordem existente
            }





            if (ordemAtualizada.getPecasUtilizadas() != null) {
                logger.info("Atualizando peças utilizadas...");
                ordemExistente.getPecasUtilizadas().clear();
                for (PecaUtilizada pecaUtilizada : ordemAtualizada.getPecasUtilizadas()) {
                    Peca peca = pecaRepository.findById(pecaUtilizada.getIdPeca())
                            .orElseThrow(() -> new RuntimeException("Peça não encontrada: ID " + pecaUtilizada.getIdPeca()));
                    pecaUtilizada.setPeca(peca);
                    pecaUtilizada.setOrdemServico(ordemExistente);
                    ordemExistente.getPecasUtilizadas().add(pecaUtilizada);
                }
            }

            // Atualizar serviços realizados
            if (ordemAtualizada.getServicosRealizados() != null) {
                logger.info("Atualizando serviços realizados...");
                ordemExistente.getServicosRealizados().clear();
                for (ServicoRealizado servicoRealizado : ordemAtualizada.getServicosRealizados()) {
                    Servico servico = servicoRepository.findById(servicoRealizado.getServico().getIdServico())
                            .orElseThrow(() -> new RuntimeException("Serviço não encontrado: ID " + servicoRealizado.getServico().getIdServico()));
                    servicoRealizado.setServico(servico); // Relacionar com o serviço existente
                    servicoRealizado.setOrdemServico(ordemExistente); // Relacionar com a ordem
                    ordemExistente.getServicosRealizados().add(servicoRealizado);
                }
            }


            ordemExistente.setDataPrevistaSaida(ordemAtualizada.getDataPrevistaSaida());
            ordemExistente.setStatus(ordemAtualizada.getStatus());
            ordemExistente.setValorTotal(ordemAtualizada.getValorTotal());



            OrdemServico ordemAtualizadaSalva = ordemServicoRepository.save(ordemExistente);

            logger.info("Ordem de serviço atualizada com sucesso: {}", ordemAtualizadaSalva);

            return ResponseEntity.ok(ordemAtualizadaSalva);

        } catch (Exception e) {
            logger.error("Erro ao atualizar a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a ordem de serviço: " + e.getMessage());
        }
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!ordemServicoRepository.existsById(id)) {
                throw new RuntimeException("Ordem de Serviço não encontrada para o ID fornecido: " + id);
            }
            ordemServicoRepository.deleteById(id);
            return ResponseEntity.ok("Ordem de serviço deletada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar a ordem de serviço: " + e.getMessage());
        }
    }

}
