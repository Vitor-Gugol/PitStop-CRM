package com.unip.pitstop.controller;

import com.unip.pitstop.model.Mecanico;
import com.unip.pitstop.model.ServicoRealizado;
import com.unip.pitstop.repository.MecanicoRepository;
import com.unip.pitstop.repository.ServicoRealizadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/mecanicos")
public class MecanicoController {

    private final MecanicoRepository mecanicoRepository;
    private final ServicoRealizadoRepository servicoRealizadoRepository;

    @Autowired
    public MecanicoController(MecanicoRepository mecanicoRepository, ServicoRealizadoRepository servicoRealizadoRepository) {
        this.mecanicoRepository = mecanicoRepository;
        this.servicoRealizadoRepository = servicoRealizadoRepository;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Long> cadastrarMecanico(@RequestBody Mecanico mecanico) {
        Mecanico novoMecanico = mecanicoRepository.save(mecanico);
        return new ResponseEntity<>(novoMecanico.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Mecanico>> listarMecanicos(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Mecanico> mecanicosPage = mecanicoRepository.findAll(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(mecanicosPage.getTotalElements()));
        return new ResponseEntity<>(mecanicosPage.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/comissoes/calcular-simples")
    public ResponseEntity<Map<String, BigDecimal>> calcularComissaoSimples(@RequestParam Long mecanicoId) {
        List<ServicoRealizado> servicosDoMecanico = servicoRealizadoRepository.findByMecanicoId(mecanicoId);
        BigDecimal totalServicos = BigDecimal.ZERO;
        for (ServicoRealizado servico : servicosDoMecanico) {
            if (servico.getPrecoCobrado() != null) {
                totalServicos = totalServicos.add(BigDecimal.valueOf(servico.getPrecoCobrado()));
            }
        }
        BigDecimal comissao = totalServicos.multiply(new BigDecimal("0.10")); // 10%
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("comissao", comissao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/comissoes/calcular")
    public ResponseEntity<Map<String, BigDecimal>> calcularComissaoPorPeriodo(
            @RequestParam Long mecanicoId,
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        List<ServicoRealizado> servicosDoMecanico = servicoRealizadoRepository.findByMecanicoIdAndPeriodo(
                mecanicoId,
                dataInicio,
                dataFim
        );

        BigDecimal totalServicos = BigDecimal.ZERO;
        for (ServicoRealizado servico : servicosDoMecanico) {
            if (servico.getPrecoCobrado() != null) {
                totalServicos = totalServicos.add(BigDecimal.valueOf(servico.getPrecoCobrado()));
            }
        }
        BigDecimal comissao = totalServicos.multiply(new BigDecimal("0.10"));
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("comissao", comissao);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Void> removerMecanico(@PathVariable Long id) {
        if (!mecanicoRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        mecanicoRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/comissoes/listar")
    public ResponseEntity<List<Map<String, Object>>> listarComissoesPorPeriodoTodosMecanicos(
            @RequestParam int ano,
            @RequestParam int mes
    ) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime dataInicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime dataFim = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        List<Mecanico> mecanicos = mecanicoRepository.findAll();
        List<Map<String, Object>> comissoesList = new ArrayList<>();

        for (Mecanico mecanico : mecanicos) {
            List<ServicoRealizado> servicosDoMecanico = servicoRealizadoRepository.findByMecanicoIdAndPeriodo(
                    mecanico.getId(),
                    dataInicio,
                    dataFim
            );

            BigDecimal totalServicos = BigDecimal.ZERO;
            for (ServicoRealizado servico : servicosDoMecanico) {
                if (servico.getPrecoCobrado() != null) {
                    totalServicos = totalServicos.add(BigDecimal.valueOf(servico.getPrecoCobrado()));
                }
            }
            BigDecimal comissao = totalServicos.multiply(new BigDecimal("0.10"));

            Map<String, Object> comissaoMecanico = new HashMap<>();
            comissaoMecanico.put("mecanicoId", mecanico.getId());
            comissaoMecanico.put("nomeMecanico", mecanico.getNome());
            comissaoMecanico.put("comissao", comissao);
            comissoesList.add(comissaoMecanico);
        }

        return ResponseEntity.ok(comissoesList);
    }
}