import React, { useState, useEffect } from "react";
import "./NovaOrdem.css";

function NovaOrdem() {
    const [ordem, setOrdem] = useState({
        clienteNome: "",
        clienteTelefone: "",
        clienteEmail: "",
        clienteEndereco: "",
        carroMarca: "",
        carroModelo: "",
        carroAno: "",
        carroPlaca: "",
        carroChassi: "",
        dataPrevistaSaida: "",
        status: "",
        pecasUtilizadas: [{ precoUnitario: "", idPeca: "", quantidade: "" }],
        servicosRealizados: [],
    });

    const [pecas, setPecas] = useState([]);
    const [servicos, setServicos] = useState([]);
    const [erros, setErros] = useState(""); // Estado para exibir mensagens de erro

    // Fetch de dados do backend
    useEffect(() => {
        const fetchData = async () => {
            try {
                const pecasResponse = await fetch("http://localhost:8080/pecas");
                const pecasData = await pecasResponse.json();
                setPecas(pecasData);

                const servicosResponse = await fetch("http://localhost:8080/servicos");
                const servicosData = await servicosResponse.json();
                setServicos(servicosData);
            } catch (error) {
                console.error("Erro ao carregar dados:", error);
            }
        };
        fetchData();
    }, []);

    // Atualiza o estado dos campos
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setOrdem((prevState) => ({ ...prevState, [name]: value }));
    };

    // Validações do formulário
    const validarFormulario = () => {
        const erros = [];
        if (!ordem.clienteNome.trim()) erros.push("O nome do cliente é obrigatório.");
        if (!ordem.clienteTelefone.trim() || ordem.clienteTelefone.length < 10) erros.push("O telefone deve ter ao menos 10 dígitos.");
        if (!ordem.clienteEmail.includes("@")) erros.push("E-mail inválido.");
        if (!ordem.carroPlaca.trim()) erros.push("A placa do carro é obrigatória.");
        if (!ordem.carroChassi.trim()) erros.push("O chassi do carro é obrigatório.");
        if (!ordem.dataPrevistaSaida) erros.push("A data prevista de saída é obrigatória.");
        if (!ordem.status.trim()) erros.push("O status da ordem é obrigatório.");
        if (!ordem.clienteEmail || !ordem.clienteEmail.includes("@")) erros.push("E-mail do cliente é obrigatório e deve ser válido.");
        return erros.length ? erros : null;
    };

    // Submissão do formulário
    const handleSubmit = async (e) => {
        e.preventDefault();
        const errosValidacao = validarFormulario();
        if (errosValidacao) {
            setErros(errosValidacao.join("\n"));
            return;
        }
        setErros("");

        const ordemServicoData = {
            cliente: {
                nome: ordem.clienteNome,
                telefone: ordem.clienteTelefone,
                email: ordem.clienteEmail,
                endereco: ordem.clienteEndereco,
            },
            carro: {
                marca: ordem.carroMarca,
                modelo: ordem.carroModelo,
                ano: Number(ordem.carroAno),
                placa: ordem.carroPlaca,
                chassi: ordem.carroChassi,
            },
            dataPrevistaSaida: ordem.dataPrevistaSaida,
            status: ordem.status,
            pecasUtilizadas: ordem.pecasUtilizadas,
            servicosRealizados: ordem.servicosRealizados,
        };

        // Mostra o JSON no console
    console.log("JSON enviado:", ordemServicoData);
        try {
            const response = await fetch("http://localhost:8080/ordens", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(ordemServicoData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Erro ao criar a ordem de serviço");
            }

            const data = await response.json();
            alert("Ordem de serviço criada com sucesso!");
            console.log("Ordem criada:", data);
        } catch (error) {
            console.error("Erro ao criar a ordem:", error);
            alert(error.message);
        }
    };

    // Adicionar uma nova peça
    const adicionarPeca = () => {
        setOrdem((prevState) => ({
            ...prevState,
            pecasUtilizadas: [...prevState.pecasUtilizadas, { idPeca: "", quantidade: "", precoUnitario: "" }],
        }));
    };

    // Adicionar um novo serviço
    const adicionarServico = () => {
        setOrdem((prevState) => ({
            ...prevState,
            servicosRealizados: [...prevState.servicosRealizados, { idServico: "", precoCobrado: "" }],
        }));
    };

    return (
        <div className="nova-ordem-container">
            <h1>Criar Nova Ordem de Serviço</h1>
            {erros && <div className="error-message">{erros}</div>} {/* Exibição de erros */}
            <form onSubmit={handleSubmit}>
                {/* Dados do Cliente */}
                <h3>Dados do Cliente</h3>
                <div className="form-group">
                    <label>Nome:</label>
                    <input name="clienteNome" type="text" value={ordem.clienteNome} onChange={handleInputChange} />
                    <label>Telefone:</label>
                    <input name="clienteTelefone" type="text" value={ordem.clienteTelefone} onChange={handleInputChange} />
                    <label>E-mail:</label>
                    <input name="clienteEmail" type="email" value={ordem.clienteEmail} onChange={handleInputChange} />
                    <label>Endereço:</label>
                    <input name="clienteEndereco" type="text" value={ordem.clienteEndereco} onChange={handleInputChange} />
                </div>

                {/* Dados do Carro */}
                <h3>Dados do Carro</h3>
                <div className="form-group">
                    <label>Marca:</label>
                    <input name="carroMarca" type="text" value={ordem.carroMarca} onChange={handleInputChange} />
                    <label>Modelo:</label>
                    <input name="carroModelo" type="text" value={ordem.carroModelo} onChange={handleInputChange} />
                    <label>Ano:</label>
                    <input name="carroAno" type="number" value={ordem.carroAno} onChange={handleInputChange} />
                    <label>Placa:</label>
                    <input name="carroPlaca" type="text" value={ordem.carroPlaca} onChange={handleInputChange} />
                    <label>Chassi:</label>
                    <input name="carroChassi" type="text" value={ordem.carroChassi} onChange={handleInputChange} />
                </div>

                {/* Peças Utilizadas */}
                <h3>Peças Utilizadas</h3>
                {ordem.pecasUtilizadas.map((peca, index) => (
                    <div key={index} className="form-group">
                        <select
                            value={peca.idPeca || ""}
                            onChange={(e) => {
                                const updatedPecas = [...ordem.pecasUtilizadas];
                                updatedPecas[index].idPeca = e.target.value;
                                setOrdem({ ...ordem, pecasUtilizadas: updatedPecas });
                            }}
                        >
                            <option value="">Selecione uma peça</option>
                            {pecas.map((peca) => (
                                <option key={peca.idPeca} value={peca.idPeca}>
                                    {peca.nome} (Estoque: {peca.quantidadeEstoque})
                                </option>
                            ))}
                        </select>
                        <input
                            type="number"
                            placeholder="Quantidade"
                            value={peca.quantidade || ""}
                            onChange={(e) => {
                                const updatedPecas = [...ordem.pecasUtilizadas];
                                updatedPecas[index].quantidade = e.target.value;
                                setOrdem({ ...ordem, pecasUtilizadas: updatedPecas });
                            }}
                        />
                        <input
                            type="number"
                            placeholder="Preço Unitário"
                            value={peca.precoUnitario || ""}
                            onChange={(e) => {
                                const updatedPecas = [...ordem.pecasUtilizadas];
                                updatedPecas[index].precoUnitario = e.target.value;
                                setOrdem({ ...ordem, pecasUtilizadas: updatedPecas });
                            }}
                        />
                    </div>
                ))}
                <button type="button" onClick={adicionarPeca}>Adicionar Peça</button>

                {/* Serviços Realizados */}
                <h3>Serviços Realizados</h3>
                {ordem.servicosRealizados.map((servico, index) => (
                    <div key={index} className="form-group">
                        <select
                            value={servico.idServico || ""}
                            onChange={(e) => {
                                const updatedServicos = [...ordem.servicosRealizados];
                                updatedServicos[index].idServico = e.target.value;
                                setOrdem({ ...ordem, servicosRealizados: updatedServicos });
                            }}
                        >
                            <option value="">Selecione um serviço</option>
                            {servicos.map((servico) => (
                                <option key={servico.idServico} value={servico.idServico}>
                                    {servico.descricao} (R$ {servico.preco})
                                </option>
                            ))}
                        </select>
                        <input
                            type="number"
                            placeholder="Preço Cobrado"
                            value={servico.precoCobrado || ""}
                            onChange={(e) => {
                                const updatedServicos = [...ordem.servicosRealizados];
                                updatedServicos[index].precoCobrado = e.target.value;
                                setOrdem({ ...ordem, servicosRealizados: updatedServicos });
                            }}
                        />
                    </div>
                ))}
                <button type="button" onClick={adicionarServico}>Adicionar Serviço</button>

                {/* Dados da Ordem de Serviço */}
                <h3>Dados da Ordem de Serviço</h3>
                <div className="form-group">
                    <label>Data Prevista de Saída:</label>
                    <input
                        name="dataPrevistaSaida"
                        type="datetime-local"
                        value={ordem.dataPrevistaSaida}
                        onChange={handleInputChange}
                    />
                    <label>Status:</label>
                    <select name="status" value={ordem.status} onChange={handleInputChange}>
                        <option value="">Selecione o status</option>
                        <option value="Em andamento">Em andamento</option>
                        <option value="Cancelado">Cancelado</option>
                        <option value="Concluído">Concluído</option>
                    </select>
                </div>

                {/* Botão de envio */}
                <button type="submit">Enviar Ordem de Serviço</button>
            </form>
        </div>
    );
}

export default NovaOrdem;
