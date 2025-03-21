package com.unip.pitstop.controller;
import com.unip.pitstop.exception.ClienteNotFoundException;


import com.unip.pitstop.model.Carro;
import com.unip.pitstop.repository.CarroRepository;
import com.unip.pitstop.repository.ClienteRepository;
import com.unip.pitstop.model.Cliente;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> cadastrar(@RequestBody Carro carro) {
        System.out.println("Marca: " + carro.getMarca());
        System.out.println("Modelo: " + carro.getModelo());
        System.out.println("Ano: " + carro.getAno());
        System.out.println("Placa: " + carro.getPlaca());


        Cliente cliente = clienteRepository.findById(carro.getCliente().getIdCliente())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado"));

        carro.setCliente(cliente);
        try {
            Carro carroSalvo = carroRepository.save(carro);
            return ResponseEntity.status(HttpStatus.CREATED).body(carroSalvo);
        } catch (Exception e) {
            e.printStackTrace(); // Exiba o erro completo nos logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar carro: " + e.getMessage());
        }
    }




    // Atualizar um carro existente
    @PutMapping("/{id}")
    public Carro atualizar(@PathVariable Long id, @RequestBody Carro carroAtualizado) {
        // Buscar o carro existente pelo ID
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));

        // Atualizar os campos do carro com os valores recebidos
        carro.setMarca(carroAtualizado.getMarca());
        carro.setModelo(carroAtualizado.getModelo());
        carro.setAno(carroAtualizado.getAno());
        carro.setPlaca(carroAtualizado.getPlaca());
        carro.setChassi(carroAtualizado.getChassi());
        carro.setCor(carroAtualizado.getCor());

        // Salvar o carro atualizado no banco
        return carroRepository.save(carro);
    }

    // Excluir um carro existente
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado"));
        carroRepository.delete(carro);
    }


}
