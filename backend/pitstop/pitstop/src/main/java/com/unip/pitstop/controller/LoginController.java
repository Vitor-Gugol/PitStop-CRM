package com.unip.pitstop.controller;

import com.unip.pitstop.model.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController // Adicionado para indicar que é um controlador REST
@RequestMapping("/login") // Adicionado para mapear a rota "/login" para essa classe
@CrossOrigin(origins = "http://localhost:3000") // Permitir requisições do React (CORS)
public class LoginController {

    @PostMapping
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        String usuarioValido = "admin";
        String senhaValida = "123456";

        if (usuarioValido.equals(loginRequest.getUsername()) && senhaValida.equals(loginRequest.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login bem-sucedido!");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
