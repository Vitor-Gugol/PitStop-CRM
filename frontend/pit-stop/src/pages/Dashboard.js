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
    }, []);

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
            {ordemSelecionada?.pecasUtilizadas && ordemSelecionada.pecasUtilizadas.length > 0 && (
                <div className="modal-section">
                    <h3>Peças Utilizadas</h3>
                    <table className="modal-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Descrição</th>
                                <th>Quantidade</th>
                                <th>Preço Unitário</th>
                            </tr>
                        </thead>
                        <tbody>
                            {ordemSelecionada.pecasUtilizadas.map((peca) => (
                                <tr key={peca.idPeca}>
                                    <td>{peca.idPeca}</td>
                                    <td>{peca.descricao}</td>
                                    <td>{peca.quantidade}</td>
                                    <td>{peca.precoUnitario}</td>
                                </tr>
                            ))}
                        </tbody>
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
                                <th>ID</th>
                                <th>Descrição</th>
                                <th>Preço Cobrado</th>
                            </tr>
                        </thead>
                        <tbody>
                            {ordemSelecionada.servicosRealizados.map((servico) => (
                                <tr key={servico.idServico}>
                                    <td>{servico.idServico}</td>
                                    <td>{servico.descricao}</td>
                                    <td>{servico.precoCobrado}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    )}
</Modal>



        </div>
    );
}

export default Dashboard;
