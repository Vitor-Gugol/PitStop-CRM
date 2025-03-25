import { Link } from "react-router-dom";
import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import "./Dashboard.css";
import NovaOrdem from "./NovaOrdem";


Modal.setAppElement("#root");

function Dashboard() {
    const [ordens, setOrdens] = useState([]);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [ordemSelecionada, setOrdemSelecionada] = useState(null);

    const [currentPage, setCurrentPage] = useState(0); 
    const [totalPages, setTotalPages] = useState(0); 
    const [pageSize] = useState(10);
    const [pecas, setPecas] = useState([]);
    const [servicos, setServicos] = useState([]);

    const [nomesPecas, setNomesPecas] = useState([]);
const [nomesServicos, setNomesServicos] = useState([]);

    const buscarOrdens = (page = 0, size = 10) => {
    fetch(`http://localhost:8080/ordens/dashboard?page=${page}&size=${size}`)
        .then((response) => response.json())
        .then((data) => {
            console.log("Dados recebidos do backend ordenados globalmente:", data);
            setOrdens(data.content);
            setCurrentPage(data.number);
            setTotalPages(data.totalPages);
        })
        .catch((error) => console.error("Erro ao buscar ordens:", error));
};
const buscarDetalhesOrdem = (id) => {
    console.log(`Chamando buscarDetalhesOrdem com id: ${id}`);
    fetch(`http://localhost:8080/ordens/detalhes/${id}`)
        .then((response) => response.json())
        .then((data) => {
            console.log("Detalhes da ordem carregados:", data); // Confirme que os dados chegaram
            setOrdemSelecionada(data);
            setModalIsOpen(true);
        })
        .catch((error) => console.error("Erro ao buscar detalhes da ordem:", error));
};



    useEffect(() => {
        buscarOrdens();
        buscarPecas();
        buscarServicos();
    }, []);

    const buscarPecas = () => {
        fetch("http://localhost:8080/pecas")
            .then((response) => response.json())
            .then((data) => {
                console.log("Peças carregadas:", data);
                setPecas(data);
            })
            .catch((error) => console.error("Erro ao buscar peças:", error));
    };

    const buscarServicos = () => {
        fetch("http://localhost:8080/servicos")
            .then((response) => response.json())
            .then((data) => {
                console.log("Serviços carregados:", data);
                setServicos(data);
            })
            .catch((error) => console.error("Erro ao buscar serviços:", error));
    };
    const abrirModal = (ordem) => {
        setOrdemSelecionada(ordem);
        setModalIsOpen(true);
    };

    const fecharModal = () => {
        setModalIsOpen(false);
        setOrdemSelecionada(null);
    };
    const salvarEdicao = () => {
        fetch(`http://localhost:8080/ordens/${ordemSelecionada.idOs}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(ordemSelecionada),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao atualizar a ordem");
                }
                return response.json();
            })
            .then((data) => {
                console.log("Ordem atualizada:", data);
                fecharModal(); // Fecha o modal
                buscarOrdens(); // Atualiza a lista na dashboard
            })
            .catch((error) => console.error("Erro ao salvar edição:", error));
    };
    const buscarDetalhesPecaServico = (id) => {
        fetch(`http://localhost:8080/ordens/detalhes/pecas-servicos/${id}`)
            .then((response) => response.json())
            .then((data) => {
                console.log("Detalhes de peças e serviços:", data);
                setOrdemSelecionada(data); // Atualiza o estado para exibir no modal
                setModalIsOpen(true); // Abre o modal
            })
            .catch((error) => console.error("Erro ao buscar detalhes de peças e serviços:", error));
    };
    
    return (
        <div className="dashboard-container">

           
           

            {/* Tabela de ordens */}
            <div className="dashboard-content">
                <h2 className="table-title">Ordens de Serviço</h2>
                <table className="dashboard-table">
                <thead>
    <tr>
        <th>Ordem de Serviço</th>
        <th>Cliente</th>
        <th>Carro</th>
        <th>Status</th>
        <th>Data de Entrada</th> {/* Nova coluna adicionada */}
        <th>Ações</th>
    
    </tr>
    
</thead>
<tbody>
    
    {ordens && Array.isArray(ordens) && ordens.map((ordem) => (
        <tr key={ordem.idOs}>
            <td>{ordem.idOs}</td>
            <td>{ordem.clienteNome}</td>
            <td>{ordem.carroModelo}</td>
            <td>{ordem.status}</td>
            <td>{new Date(ordem.dataEntrada).toLocaleDateString()}</td> {/* Exibe a data formatada */}
            <td>
            <button
                className="editar-btn"
                onClick={() => buscarDetalhesOrdem(ordem.idOs)} // Passa o ID para a função
            >
                Editar
            </button>



            </td>
        </tr>
    ))}
</tbody>



                </table>
                
            </div>

            {/* Botões de paginação */}
            <div className="pagination-controls">
                <button
                    className="pagination-btn"
                    onClick={() => buscarOrdens(currentPage - 1, pageSize)}
                    disabled={currentPage === 0}
                >
                    Anterior
                </button>
                <span>
                    Página {currentPage + 1} de {totalPages}
                </span>
                <button
                    className="pagination-btn"
                    onClick={() => buscarOrdens(currentPage + 1, pageSize)}
                    disabled={currentPage === totalPages - 1}
                >
                    Próxima
                </button>
            </div>
            
            <Link to="/nova-ordem">
                    <button className="nova-ordem-btn">Criar Nova Ordem</button>
                </Link>

            {/* Modal de edição */}
         <Modal
    isOpen={modalIsOpen}
    onRequestClose={fecharModal}
    className="modal"
    overlayClassName="overlay"
    contentLabel="Editar Ordem"
>
    {ordemSelecionada && (
        <div className="modal-body">
            <h2 className="modal-title">Editar Ordem de Serviço</h2>
            <form className="modal-form">
                {/* Campo para Cliente */}
                <label className="form-label">
                    Cliente:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.clienteNome || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                clienteNome: e.target.value,
                            })
                        }
                    />
                </label>

                {/* Campo para Carro */}
                <label className="form-label">
                    Carro:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.carroModelo || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                carroModelo: e.target.value,
                            })
                        }
                    />
                </label>

                {/* Campo para Placa */}
                <label className="form-label">
                    Placa:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.carroPlaca || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                carroPlaca: e.target.value,
                            })
                        }
                    />
                </label>

                {/* Campo para Data Prevista de Saída */}
                <label className="form-label">
                    Data Prevista de Saída:
                    <input
                        className="form-input"
                        type="date"
                        value={ordemSelecionada.dataPrevistaSaida || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                dataPrevistaSaida: e.target.value,
                            })
                        }
                    />
                </label>

                {/* Campo para Valor Total */}
                <label className="form-label">
                    Valor Total:
                    <input
                        className="form-input"
                        type="number"
                        value={ordemSelecionada.valorTotal || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                valorTotal: e.target.value,
                            })
                        }
                    />
                </label>

                {/* Campo para Status */}
                <label className="form-label">
                    Status:
                    <select
                        className="form-select"
                        value={ordemSelecionada.status || ""} // Pré-preenchido ou vazio
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                status: e.target.value,
                            })
                        }
                    >
                        <option value="Em andamento">Em andamento</option>
                        <option value="Concluído">Concluído</option>
                        <option value="Cancelado">Cancelado</option>
                    </select>
                </label>
            </form>

           {/* Exibição das Peças Utilizadas */}
{/* Lista de Peças Utilizadas */}
{ordemSelecionada?.pecasUtilizadas && ordemSelecionada.pecasUtilizadas.length > 0 && (
    <div className="modal-section">
        <h3>Peças Utilizadas</h3>
        <table className="modal-table">
            <thead>
                <tr>
                    <th>Nome da Peça</th>
                    <th>Quantidade</th>
                    <th>Preço Unitário</th>
                </tr>
            </thead>
            <tbody>
    {ordemSelecionada.pecasUtilizadas.map((peca, index) => (
        <tr key={index}>
            <td>
                <select
                    value={peca.idPeca || ""}
                    onChange={(e) => {
                        const novasPecas = [...ordemSelecionada.pecasUtilizadas];
                        novasPecas[index].idPeca = parseInt(e.target.value, 10);
                        setOrdemSelecionada({
                            ...ordemSelecionada,
                            pecasUtilizadas: novasPecas,
                        });
                    }}
                >
                    <option value="">Selecione uma peça</option>
                    {pecas.map((peca) => (
                        <option key={peca.idPeca} value={peca.idPeca}>
                            {peca.nome}
                        </option>
                    ))}
                </select>
            </td>
            <td>
                <input
                    type="number"
                    value={peca.quantidade || ""}
                    onChange={(e) => {
                        const novasPecas = [...ordemSelecionada.pecasUtilizadas];
                        novasPecas[index].quantidade = parseInt(e.target.value, 10) || 0;
                        setOrdemSelecionada({
                            ...ordemSelecionada,
                            pecasUtilizadas: novasPecas,
                        });
                    }}
                />
            </td>
            <td>
                <input
                    type="number"
                    value={peca.precoUnitario || ""}
                    onChange={(e) => {
                        const novasPecas = [...ordemSelecionada.pecasUtilizadas];
                        novasPecas[index].precoUnitario = parseFloat(e.target.value) || 0;
                        setOrdemSelecionada({
                            ...ordemSelecionada,
                            pecasUtilizadas: novasPecas,
                        });
                    }}
                />
            </td>
        </tr>
    ))}
</tbody>
<button
    onClick={() => {
        const novasPecas = [...ordemSelecionada.pecasUtilizadas, { idPeca: "", quantidade: 0, precoUnitario: 0 }];
        setOrdemSelecionada({ ...ordemSelecionada, pecasUtilizadas: novasPecas });
    }}
>
    Adicionar Peça
</button>


        </table>
    </div>
)}


{/* Exibição dos Serviços Realizados */}
{ordemSelecionada?.servicosRealizados && ordemSelecionada.servicosRealizados.length > 0 && (
    <div className="modal-section">
        <h3>Serviços Realizados</h3>
        <table className="modal-table">
            <thead>
                <tr>
                    <th>Nome do Serviço</th>
                    <th>Preço Cobrado</th>
                </tr>
            </thead>
            <tbody>
    {ordemSelecionada.servicosRealizados.map((servico, index) => (
        <tr key={index}>
            <td>
                <select
                    value={servico.idServico || ""}
                    onChange={(e) => {
                        const novosServicos = [...ordemSelecionada.servicosRealizados];
                        novosServicos[index].idServico = parseInt(e.target.value, 10);
                        setOrdemSelecionada({
                            ...ordemSelecionada,
                            servicosRealizados: novosServicos,
                        });
                    }}
                >
                    <option value="">Selecione um serviço</option>
                    {servicos.map((servico) => (
                        <option key={servico.idServico} value={servico.idServico}>
                            {servico.descricao}
                        </option>
                    ))}
                </select>
            </td>
            <td>
                <input
                    type="number"
                    value={servico.precoCobrado || ""}
                    onChange={(e) => {
                        const novosServicos = [...ordemSelecionada.servicosRealizados];
                        novosServicos[index].precoCobrado = parseFloat(e.target.value) || 0;
                        setOrdemSelecionada({
                            ...ordemSelecionada,
                            servicosRealizados: novosServicos,
                        });
                    }}
                />
            </td>
        </tr>
    ))}
</tbody>

        </table>
        <button
    onClick={() => {
        const novosServicos = [...ordemSelecionada.servicosRealizados, { idServico: "", precoCobrado: 0 }];
        setOrdemSelecionada({ ...ordemSelecionada, servicosRealizados: novosServicos });
    }}
>
    Adicionar Serviço
</button>

        <button
            className="adicionar-servico-btn"
            onClick={() => {
                const novosServicos = [...ordemSelecionada.servicosRealizados, { nome: "", precoCobrado: 0 }];
                setOrdemSelecionada({
                    ...ordemSelecionada,
                    servicosRealizados: novosServicos,
                });
            }}
        >
            Adicionar Outro Serviço
        </button>


    </div>
)}


        </div>
    )}
</Modal>



        </div>
    );
}

export default Dashboard;
