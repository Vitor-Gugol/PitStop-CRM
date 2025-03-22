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
                console.log("Dados recebidos do backend:", data);
                setOrdens(data.content);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            })
            .catch((error) => console.error("Erro ao buscar ordens:", error));
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

    return (
        <div className="dashboard-container">
            {/* Botão para Criar Nova Ordem */}
           
           <Link to="/nova-ordem">
                    <button className="nova-ordem-btn">Criar Nova Ordem</button>
                </Link>
           

            {/* Tabela de ordens */}
            <div className="dashboard-content">
                <h2 className="table-title">Ordens de Serviço</h2>
                <table className="dashboard-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Cliente</th>
                            <th>Carro</th>
                            <th>Status</th>
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
            <td>
                <button
                    className="editar-btn"
                    onClick={() => abrirModal(ordem)}
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

            {/* Modal de edição */}
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={fecharModal}
                className="modal"
                overlayClassName="overlay"
                contentLabel="Editar Ordem"
            >
                {ordemSelecionada && (
                    <div>
                        <h2>Editar Ordem {ordemSelecionada.idOs}</h2>
                        <form>
                            <label>
                                Data Prevista de Saída:
                                <input
                                    type="text"
                                    defaultValue={ordemSelecionada.dataPrevistaSaida}
                                    onChange={(e) =>
                                        setOrdemSelecionada({
                                            ...ordemSelecionada,
                                            dataPrevistaSaida: e.target.value,
                                        })
                                    }
                                />
                            </label>
                            <label>
                                Status:
                                <input
                                    type="text"
                                    defaultValue={ordemSelecionada.status}
                                    onChange={(e) =>
                                        setOrdemSelecionada({
                                            ...ordemSelecionada,
                                            status: e.target.value,
                                        })
                                    }
                                />
                            </label>
                            <label>
                                Valor Total:
                                <input
                                    type="number"
                                    defaultValue={ordemSelecionada.valorTotal}
                                    onChange={(e) =>
                                        setOrdemSelecionada({
                                            ...ordemSelecionada,
                                            valorTotal: e.target.value,
                                        })
                                    }
                                />
                            </label>
                            <div className="modal-actions">
                                <button type="button" onClick={fecharModal}>
                                    Cancelar
                                </button>
                            </div>
                        </form>
                    </div>
                )}
            </Modal>
        </div>
    );
}

export default Dashboard;
