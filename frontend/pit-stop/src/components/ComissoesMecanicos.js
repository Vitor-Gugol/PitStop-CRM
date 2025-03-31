
import React, { useState } from 'react';
import styles from './ComissoesMecanicos.module.css';

function ComissoesMecanicos({ onComissoesFetched }) {
    const [ano, setAno] = useState(new Date().getFullYear());
    const [mes, setMes] = useState(new Date().getMonth() + 1);

    const handleAnoChange = (event) => {
        setAno(parseInt(event.target.value));
    };

    const handleMesChange = (event) => {
        setMes(parseInt(event.target.value));
    };

    const buscarComissoes = async () => {
        try {
            const response = await fetch(
                `http://localhost:8080/mecanicos/comissoes/listar?ano=${ano}&mes=${mes}`
            );

            if (!response.ok) {
                const errorData = await response.json();
                onComissoesFetched([], errorData.message || 'Erro ao buscar comissões.');
                return;
            }

            const data = await response.json();
            const comissoesComPeriodo = data.map(item => ({ ...item, ano, mes }));
            onComissoesFetched(comissoesComPeriodo, '');
        } catch (error) {
            console.error('Erro ao buscar comissões:', error);
            onComissoesFetched([], 'Erro ao buscar comissões.');
        }
    };

    return (
        <div className={styles.containerComissoes}>
            <h2>Comissões dos Mecânicos</h2>
            <div>
                <label htmlFor="ano">Ano:</label>
                <input
                    type="number"
                    id="ano"
                    value={ano}
                    onChange={handleAnoChange}
                />
                <label htmlFor="mes">Mês:</label>
                <select id="mes" value={mes} onChange={handleMesChange}>
                    <option value={1}>Janeiro</option>
                    <option value={2}>Fevereiro</option>
                    <option value={3}>Março</option>
                    <option value={4}>Abril</option>
                    <option value={5}>Maio</option>
                    <option value={6}>Junho</option>
                    <option value={7}>Julho</option>
                    <option value={8}>Agosto</option>
                    <option value={9}>Setembro</option>
                    <option value={10}>Outubro</option>
                    <option value={11}>Novembro</option>
                    <option value={12}>Dezembro</option>
                </select>
                <div className='btnComiss'>
                <button onClick={buscarComissoes}>Buscar Comissões</button>
                </div>
            </div>
        
        </div>
    );
}

export default ComissoesMecanicos;