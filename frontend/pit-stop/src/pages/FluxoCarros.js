
import React, { useState, useEffect } from 'react';
import styles from './FluxoCarros.module.css';

function FluxoCarros() {
    const [ordensServico, setOrdensServico] = useState([]);
    const [filtroStatus, setFiltroStatus] = useState(''); 
    const [erro, setErro] = useState('');

    useEffect(() => {
        carregarOrdensServico();
    }, [filtroStatus]);

    function formatDate(dateArray) {
      if (!dateArray || !Array.isArray(dateArray)) {
          return 'Data Inválida';
      }
  
      let year, month, day, hour, minute, second = 0, millisecond = 0;
  
      if (dateArray.length === 7) {
          [year, month, day, hour, minute, second, millisecond] = dateArray;
      } else if (dateArray.length === 5) {
          [year, month, day, hour, minute] = dateArray;
      } else {
          return 'Formato de Data Inválido';
      }
  
      const dateObject = new Date(year, month - 1, day, hour, minute, second, millisecond);
      return dateObject.toLocaleDateString('pt-BR', { hour: 'numeric', minute: 'numeric' });
  }
    const carregarOrdensServico = async () => {
        try {
            const statusQuery = filtroStatus ? `status=${filtroStatus}` : '';
            const response = await fetch(`/ordens/fluxo-carros${statusQuery ? '?' + statusQuery : ''}`);
            if (!response.ok) {
                throw new Error(`Erro ao carregar ordens de serviço: ${response.status}`);
            }
            const data = await response.json();
            setOrdensServico(data);
        } catch (error) {
            console.error(error);
            setErro('Erro ao carregar ordens de serviço.');
        }
    };

    const handleFiltroChange = (event) => {
        setFiltroStatus(event.target.value);
    };

    return (
        <div className={styles.container}>
            <h1>Fluxo de Carros</h1>

            <div className={styles.filtros}>
                <select id="filtroStatus" value={filtroStatus} onChange={handleFiltroChange}>
                    <option value="">Todos</option>
                    <option value="em andamento">Em Andamento</option>
                    <option value="concluído">Concluído</option>
                    <option value="cancelado">Cancelado</option>
                </select>
            </div>

            <div className={styles.listaOrdens}>
                {erro && <p className={styles.erro}>{erro}</p>}
                {ordensServico.length > 0 ? (
                   <div className={styles.tabelaContainer}>
                    <table className={styles.tabelaCarros}>
                        <thead>
                            <tr>
                                <th>Placa</th>
                                <th>Modelo</th>
                                <th>Cliente</th>
                                <th>Data de Entrada</th>
                                <th>Data de Saída</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                        {ordensServico.map(ordem => {
    console.log('Dados da Ordem:', ordem);
    return (
        <tr key={ordem.idOs}>
          <td>{ordem.placa}</td>
            <td>{ordem.modelo}</td>
            <td>{ordem.clienteNome}</td>
            <td>{formatDate(ordem.dataEntrada)}</td>
            <td>{ordem.dataSaida ? formatDate(ordem.dataSaida) : 'Pendente'}</td>
            <td>{ordem.status || "Não informado"}</td>
        </tr>
    );
})}
                        </tbody>
                    </table>
                    </div>
                ) : (
                    <p>Nenhuma ordem de serviço encontrada com o filtro aplicado.</p>
                )}
            </div>
        </div>
    );
}

export default FluxoCarros;