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

import java.time.LocalDateTime;
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


    private static final Logger logger = LoggerFactory.getLogger(OrdemServicoController.class);
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

    @Autowired
    private  MecanicoRepository mecanicoRepository;



    public OrdemServicoController(OrdemServicoRepository ordemServicoRepository, ClienteRepository clienteRepository, CarroRepository carroRepository, PecaRepository pecaRepository, ServicoRepository servicoRepository, MecanicoRepository mecanicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.carroRepository = carroRepository;
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
        this.mecanicoRepository = mecanicoRepository;
    }


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

    @GetMapping("/detalhes/{id}")
    public OrdemServicoDetalhesDTO buscarDetalhesOrdem(@PathVariable Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

        List<ServicoRealizadoDetalheDTO> servicosDetalhados = ordemServico.getServicosRealizados().stream()
                .map(sr -> {
                    Servico servico = sr.getServico();
                    return new ServicoRealizadoDetalheDTO(
                            sr.getIdServicoRealizado(),
                            servico != null ? servico.getIdServico() : null,
                            servico != null ? servico.getDescricao() : null,
                            sr.getPrecoCobrado()
                    );
                })
                .collect(Collectors.toList());

        List<PecaUtilizadaDetalheDTO> pecasDetalhadas = ordemServico.getPecasUtilizadas().stream()
                .map(pu -> {
                    Peca peca = pu.getPeca();
                    return new PecaUtilizadaDetalheDTO(
                            pu.getIdPecaUtilizada(),
                            peca != null ? peca.getIdPeca() : null,
                            peca != null ? peca.getNome() : null,
                            pu.getQuantidade(),
                            pu.getPrecoUnitario()
                    );
                })
                .collect(Collectors.toList());

        return new OrdemServicoDetalhesDTO(
                ordemServico.getIdOs(),
                ordemServico.getCliente() != null ? ordemServico.getCliente().getNome() : "Cliente não informado",
                ordemServico.getCarro() != null ? ordemServico.getCarro().getModelo() : "Carro não informado",
                ordemServico.getCarro() != null ? ordemServico.getCarro().getPlaca() : "Placa não informada",
                ordemServico.getDataEntrada() != null ? ordemServico.getDataEntrada().toString() : "Data não informada",
                ordemServico.getDataPrevistaSaida() != null ? ordemServico.getDataPrevistaSaida().toString() : "Data não informada",
                ordemServico.getValorTotal() != null ? ordemServico.getValorTotal().toString() : "Valor não informado",
                ordemServico.getStatus(),
                servicosDetalhados,
                pecasDetalhadas
        );
    }

    @GetMapping("/detalhes/pecas-servicos/{id}")
    public OrdemDetalhesPecaServicoDTO buscarDetalhesPecaServico(@PathVariable Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));

        List<PecaUtilizadaDTO> pecasDTO = ordemServico.getPecasUtilizadas().stream()
                .map(pecaUtilizada -> new PecaUtilizadaDTO(
                        pecaUtilizada.getPeca().getNome(),
                        pecaUtilizada.getQuantidade(),
                        pecaUtilizada.getPrecoUnitario()
                ))
                .collect(Collectors.toList());



        List<ServicoRealizadoDTO> servicosDTO = ordemServico.getServicosRealizados().stream()
                .map(servicoRealizado -> new ServicoRealizadoDTO(
                        servicoRealizado.getNome(),
                        servicoRealizado.getPrecoCobrado()
                ))
                .collect(Collectors.toList());




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
                .map(Servico::getDescricao)
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
                    if (pecaUtilizada.getIdPeca() == null) {
                        throw new RuntimeException("O campo idPeca é obrigatório.");
                    }

                    Peca pecaExistente = pecaRepository.findById(pecaUtilizada.getIdPeca())
                            .orElseThrow(() -> new RuntimeException("Peça não encontrada com ID: " + pecaUtilizada.getIdPeca()));


                    pecaUtilizada.setPeca(pecaExistente);
                    pecaUtilizada.setOrdemServico(ordemServico);
                }
            }





            if (ordemServico.getServicosRealizados() != null) {
                for (ServicoRealizado servicoRealizado : ordemServico.getServicosRealizados()) {
                    if (servicoRealizado.getIdServico() == null) {
                        throw new RuntimeException("O campo idServico é obrigatório.");
                    }

                    Servico servicoExistente = servicoRepository.findById(servicoRealizado.getIdServico())
                            .orElseThrow(() -> new RuntimeException("Serviço não encontrado com ID: " + servicoRealizado.getIdServico()));

                    servicoRealizado.setServico(servicoExistente);
                    servicoRealizado.setOrdemServico(ordemServico);

                    if (servicoRealizado.getMecanicoId() == null) {
                        throw new RuntimeException("O campo mecanicoId é obrigatório para cada serviço realizado.");
                    }
                    Mecanico mecanicoResponsavel = mecanicoRepository.findById(servicoRealizado.getMecanicoId())
                            .orElseThrow(() -> new RuntimeException("Mecânico não encontrado com ID: " + servicoRealizado.getMecanicoId()));
                    servicoRealizado.setMecanicoId(mecanicoResponsavel.getId());
                }
            }




            OrdemServico ordemSalva = ordemServicoRepository.save(ordemServico);
            logger.info("Ordem de serviço salva com sucesso: {}", ordemSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(ordemSalva);

        } catch (Exception e) {
            logger.error("Erro ao salvar a ordem de serviço: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao salvar a ordem de serviço: " + e.getMessage()));

        }
    }

    @GetMapping("/fluxo-carros")
    public ResponseEntity<List<FluxoCarrosDTO>> listarOrdensParaFluxoCarros(
            @RequestParam(name = "status", required = false) String status) {
        List<OrdemServico> ordens;

        if (status != null && !status.isEmpty()) {
            ordens = ordemServicoRepository.findByStatusIgnoreCase(status);
        } else {
            ordens = ordemServicoRepository.findAll();
        }

        List<FluxoCarrosDTO> fluxoCarrosDTOList = ordens.stream()
                .map(ordem -> {
                    LocalDateTime dataSaida = null;
                    if (ordem.getStatus() != null && ordem.getStatus().equalsIgnoreCase("concluído") && ordem.getDataPrevistaSaida() != null) {
                        dataSaida = ordem.getDataPrevistaSaida();
                    }
                    return new FluxoCarrosDTO(
                            ordem.getIdOs(),
                            ordem.getCarro() != null ? ordem.getCarro().getPlaca() : "Placa não informada",
                            ordem.getCarro() != null ? ordem.getCarro().getModelo() : "Modelo não informado",
                            ordem.getCliente() != null ? ordem.getCliente().getNome() : "Cliente não informado",
                            ordem.getDataEntrada(),
                            dataSaida,
                            ordem.getStatus()
                    );
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(fluxoCarrosDTOList, HttpStatus.OK);
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
                ordemExistente.setCliente(cliente);
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
                ordemExistente.setCarro(carro);
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


            if (ordemAtualizada.getServicosRealizados() != null) {

                List<Long> servicosRealizadosProcessados = new ArrayList<>();

                for (ServicoRealizado servicoRealizadoDTO : ordemAtualizada.getServicosRealizados()) {
                    Servico servico = servicoRepository.findById(servicoRealizadoDTO.getIdServico())
                            .orElseThrow(() -> new RuntimeException("Serviço não encontrado: ID " + servicoRealizadoDTO.getIdServico()));

                    if (servicoRealizadoDTO.getIdServicoRealizado() != null) {

                        ServicoRealizado servicoRealizadoExistente = ordemExistente.getServicosRealizados().stream()
                                .filter(sr -> servicoRealizadoDTO.getIdServicoRealizado().equals(sr.getIdServicoRealizado()))
                                .findFirst()
                                .orElse(null);

                        if (servicoRealizadoExistente != null) {
                            servicoRealizadoExistente.setServico(servico);
                            servicoRealizadoExistente.setPrecoCobrado(servicoRealizadoDTO.getPrecoCobrado());
                            servicosRealizadosProcessados.add(servicoRealizadoExistente.getIdServicoRealizado());
                        } else {

                            logger.error("Tentativa de atualizar ServicoRealizado com ID {} que não está associado à Ordem {}", servicoRealizadoDTO.getIdServicoRealizado(), id);

                        }
                    } else {

                        ServicoRealizado novoServicoRealizado = new ServicoRealizado();
                        novoServicoRealizado.setServico(servico);
                        novoServicoRealizado.setPrecoCobrado(servicoRealizadoDTO.getPrecoCobrado());
                        novoServicoRealizado.setOrdemServico(ordemExistente);
                        ordemExistente.getServicosRealizados().add(novoServicoRealizado);
                    }
                }

                // Remover serviços realizados que não foram incluídos na atualização
                ordemExistente.getServicosRealizados().removeIf(sr -> !servicosRealizadosProcessados.contains(sr.getIdServicoRealizado()));
            } else {
                // Se servicosRealizados for null na ordem atualizada, remover todos os existentes
                ordemExistente.getServicosRealizados().clear();
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
