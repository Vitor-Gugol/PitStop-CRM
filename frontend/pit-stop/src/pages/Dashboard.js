import { Link } from "react-router-dom";
import React, { useEffect, useState } from "react";
import Modal from "react-modal";
import "./Dashboard.css";
import NovaOrdem from "./NovaOrdem";


Modal.setAppElement("#root");

function Dashboard() {
    const [ordens, setOrdens] = useState([]);
    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [ordemSelecionada, setOrdemSelecionada] = useState({
        idOs: null,
        clienteNome: "",
        carroModelo: "",
        carroPlaca: "",
        dataEntrada: "",
        dataPrevistaSaida: "",
        pecasUtilizadas: [],
        servicosRealizados: [],
        status: "",
        valorTotal: "",
    });
    

    const [currentPage, setCurrentPage] = useState(0); 
    const [totalPages, setTotalPages] = useState(0); 
    const [pageSize] = useState(10);
    const [pecas, setPecas] = useState([]);
    const [servicos, setServicos] = useState([]);
    
    const handleInputChange = (e) => {
        const { name, value } = e.target;
    
        setOrdemSelecionada((prevState) => ({
            ...prevState,
            [name]: name === "dataPrevistaSaida" ? value + ":00" : value, 
        }));
    };
    
    
    
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
            console.log("Detalhes da ordem carregados:", data); 
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
    const abrirModal = () => {
        if (!modalIsOpen) {
            setModalIsOpen(true);
        }
    };

    const fecharModal = () => {
        if (modalIsOpen) {
            setModalIsOpen(false);
            setOrdemSelecionada({
                idOs: null,
                clienteNome: "",
                carroModelo: "",
                carroPlaca: "",
                dataEntrada: "",
                dataPrevistaSaida: "",
                pecasUtilizadas: [],
                servicosRealizados: [],
                status: "",
                valorTotal: "",
            });
        }
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
                fecharModal();
                buscarOrdens(); 
            })
            .catch((error) => {
                console.error("Detalhes do erro:", error);
                alert(error.message || "Erro ao salvar edição. Verifique os dados e tente novamente.");
            });
            

            console.log("Dados enviados ao backend:", ordemSelecionada);

    };
    console.log("Data Prevista de Saída enviada:", ordemSelecionada.dataPrevistaSaida);
 

    const calcularPrecoTotal = () => {
        let total = 0;
    
        // Somar valores das peças utilizadas
        if (ordemSelecionada.pecasUtilizadas && ordemSelecionada.pecasUtilizadas.length > 0) {
            total += ordemSelecionada.pecasUtilizadas.reduce(
                (acc, peca) => acc + (peca.quantidade * peca.precoUnitario || 0),
                0
            );
        }
    
        // Somar valores dos serviços realizados
        if (ordemSelecionada.servicosRealizados && ordemSelecionada.servicosRealizados.length > 0) {
            total += ordemSelecionada.servicosRealizados.reduce(
                (acc, servico) => acc + (servico.precoCobrado || 0),
                0
            );
        }
    
        // Atualizar o estado com o valor total
        setOrdemSelecionada((prevState) => ({
            ...prevState,
            valorTotal: total,
        }));
    };
    
    const excluirOrdem = (id) => {
        fetch(`http://localhost:8080/ordens/${id}`, {
            method: "DELETE",
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Erro ao excluir a ordem de serviço");
                }
                console.log(`Ordem de serviço com ID ${id} excluída com sucesso.`);
                buscarOrdens(); // Atualiza a lista de ordens
            })
            .catch((error) => console.error("Erro ao excluir a ordem de serviço:", error));
    };
    
    
    
    const buscarDetalhesPecaServico = (id) => {
        fetch(`http://localhost:8080/ordens/detalhes/pecas-servicos/${id}`)
            .then((response) => response.json())
            .then((data) => {
                console.log("Detalhes de peças e serviços:", data);
                setOrdemSelecionada(data); 
                setModalIsOpen(true); 
            })
            .catch((error) => console.error("Erro ao buscar detalhes de peças e serviços:", error));
    };
    console.log("Data enviada ao backend:", ordemSelecionada.dataPrevistaSaida);
 
    
    
    return (
        <div className="dashboard-container">

           
           

          
            <div className="dashboard-content">
                <h2 className="table-title">Ordens de Serviço</h2>
                <table className="dashboard-table">
                <thead>
    <tr>
        <th>Ordem de Serviço</th>
        <th>Cliente</th>
        <th>Carro</th>
        <th>Status</th>
        <th>Data de Entrada</th>
        <th>Ações</th>
    
    </tr>
    
</thead>
<tbody>
    
    {ordens && Array.isArray(ordens) && ordens.map((ordem) => (
        <tr key={ordem.idOs}>
            <td>{ordem.idOs}</td>
            <td>{ordem.clienteNome || "Não informado"}</td>
            <td>{ordem.carroModelo || "Não informado"}</td>
            <td>{ordem.status || "Não informado" }</td>
            <td>{new Date(ordem.dataEntrada).toLocaleDateString()}</td> 
            <td>
            <button
                className="editar-btn"
                onClick={() => buscarDetalhesOrdem(ordem.idOs)} 
            >
                Editar
            </button>
             {/* Botão Excluir */}
             <button
                    className="excluir-btn"
                    onClick={() => excluirOrdem(ordem.idOs)}
                >
                    Excluir
                </button>


            </td>
        </tr>
    ))}
</tbody>



                </table>
                
            </div>

            
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
       

                <label className="form-label">
                    Cliente:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.clienteNome || ""} 
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                clienteNome: e.target.value,
                            })
                        }
                    />
                </label>

            
                <label className="form-label">
                    Carro:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.carroModelo || ""} 
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                carroModelo: e.target.value,
                            })
                        }
                    />
                </label>

              
                <label className="form-label">
                    Placa:
                    <input
                        className="form-input"
                        type="text"
                        value={ordemSelecionada.carroPlaca || ""} 
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                carroPlaca: e.target.value,
                            })
                        }
                    />
                </label>

                <label className="form-label">
    Data Prevista de Saída:
    <input
    name="dataPrevistaSaida"
    type="datetime-local"
    value={ordemSelecionada.dataPrevistaSaida?.slice(0, 16) || ""} 
    onChange={handleInputChange}
/>



</label>









          
                <label className="form-label">
                    Valor Total:
                    <input
                        className="form-input"
                        type="number"
                        value={ordemSelecionada.valorTotal || ""} 
                        onChange={(e) =>
                            setOrdemSelecionada({
                                ...ordemSelecionada,
                                valorTotal: e.target.value,
                            })
                        }
                    />
                </label>

           
                <label className="form-label">
                    Status:
                    <select
                        className="form-select"
                        value={ordemSelecionada.status || ""} 
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
                        calcularPrecoTotal(); // Recalcula o preço total
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
                        calcularPrecoTotal(); // Recalcula o preço total
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
                        calcularPrecoTotal(); // Recalcula o preço total
                    }}
                />
            </td>
        </tr>
    ))}
</tbody>

        </table>
       

       

    </div>
)}


        </div>
    )}
    <button
    onClick={(e) => {
        e.preventDefault(); 
        salvarEdicao(); 
    }}
>
    Enviar Atualização
</button>

</Modal>



        </div>
        
    );
}

export default Dashboard;
