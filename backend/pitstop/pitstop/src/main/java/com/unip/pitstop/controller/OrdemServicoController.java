package com.unip.pitstop.controller;

import com.unip.pitstop.dto.OrdemServicoDTO;
import com.unip.pitstop.model.OrdemServico;
import com.unip.pitstop.repository.OrdemServicoRepository;
import com.unip.pitstop.repository.ClienteRepository;
import com.unip.pitstop.repository.CarroRepository;
import com.unip.pitstop.model.Cliente;
import com.unip.pitstop.model.Carro;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;



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



    // Cadastrar uma nova ordem de serviço
    @PostMapping
    public OrdemServico cadastrar(@Valid @RequestBody OrdemServico ordemServico) {
        // Verificar se o cliente existe
        Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        ordemServico.setCliente(cliente);

        // Verificar se o carro existe
        Carro carro = carroRepository.findById(ordemServico.getCarro().getIdCarro())
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        ordemServico.setCarro(carro);

        return ordemServicoRepository.save(ordemServico);
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
