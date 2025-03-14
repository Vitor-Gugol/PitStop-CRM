package com.unip.pitstop.controller;

import com.unip.pitstop.model.Carro;
import com.unip.pitstop.repository.CarroRepository;
import com.unip.pitstop.repository.ClienteRepository;
import com.unip.pitstop.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Listar todos os carros
    @GetMapping
    public List<Carro> listarTodos() {
        return carroRepository.findAll();
    }

    // Cadastrar um novo carro
    @PostMapping
    public Carro cadastrar(@RequestBody Carro carro) {
        // Valida se cliente existe
        Cliente cliente = clienteRepository.findById(carro.getCliente().getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        carro.setCliente(cliente);
        return carroRepository.save(carro);
    }

    // Atualizar e excluir endpoints podem ser implementados de forma similar
}
