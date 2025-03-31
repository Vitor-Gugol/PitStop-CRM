import React, { useState } from "react";
import "./Login.css";
import { useNavigate } from "react-router-dom";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate(); 

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("http://localhost:8080/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            console.log("Resposta do backend:", response);

            if (response.ok) {
                const data = await response.text(); // Receber como texto, já que o backend retorna texto
                console.log("Login bem-sucedido:", data);
                setError(""); 
                alert(data); 
                navigate("/dashboard"); // Redirecionar para o dashboard após o sucesso
            } else {
                const errorMessage = await response.text();
                setError(errorMessage || "Credenciais inválidas. Tente novamente!");
            }
        } catch (err) {
            console.error("Erro ao fazer login:", err);
            setError("Erro ao conectar ao servidor. Tente mais tarde.");
        }
    };

    return (
        <div className="login-container">
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Usuário</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Digite seu usuário"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">Senha</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Digite sua senha"
                        required
                    />
                </div>
                <button type="submit" className="login-btn">Entrar</button>
                {error && <p className="error-message">{error}</p>} 
            </form>
        </div>
    );
}

export default Login;
