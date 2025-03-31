// src/pages/ControleMecanicos.js
import React, { useState, useEffect, useRef, useCallback } from 'react';
import styles from './ControleMecanicos.module.css';
import MecanicoForm from '../components/MecanicoForm';
import ComissoesMecanicos from '../components/ComissoesMecanicos';
import Modal from 'react-modal'; // Importe o componente Modal

// Defina o elemento raiz para o react-modal (geralmente o body)
Modal.setAppElement('#root'); // Ou o ID do seu elemento raiz

const initialLimit = 5;
const initialOffset = 0;

function ControleMecanicos() {
    const [isCadastroVisivel, setIsCadastroVisivel] = useState(false);
    const [mecanicos, setMecanicos] = useState([]);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [offset, setOffset] = useState(initialOffset);
    const [totalMecanicos, setTotalMecanicos] = useState(0);
    const [erroRemover, setErroRemover] = useState('');
    const [isComissoesModalVisivel, setIsComissoesModalVisivel] = useState(false);
    const [dadosComissoes, setDadosComissoes] = useState([]);
    const [erroComissoes, setErroComissoes] = useState('');

    const observer = useRef();
    const lastMecanicoElementRef = useCallback(node => {
        if (loading) return;
        if (observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                setOffset(prevOffset => prevOffset + initialLimit);
            }
        });
        if (node) observer.current.observe(node);
    }, [loading, hasMore]);

    const handleAbrirCadastro = () => {
        setIsCadastroVisivel(true);
    };

    const handleFecharCadastro = () => {
        setIsCadastroVisivel(false);
    };

    const handleMecanicoCadastrado = () => {
        console.log('Mecânico cadastrado na tela de controle.');
        handleFecharCadastro();
        setMecanicos([]);
        setOffset(initialOffset);
        setHasMore(true);
    };

    const abrirModalComissoes = (comissoes, erro) => {
        setDadosComissoes(comissoes);
        setErroComissoes(erro);
        setIsComissoesModalVisivel(true);
    };

    const fecharModalComissoes = () => {
        setIsComissoesModalVisivel(false);
        setDadosComissoes([]);
        setErroComissoes('');
    };

    useEffect(() => {
        setLoading(true);
        fetch(`/mecanicos/listar?offset=${offset}&limit=${initialLimit}`)
            .then(response => {
                const totalCount = response.headers.get('X-Total-Count');
                if (totalCount) {
                    setTotalMecanicos(parseInt(totalCount, 10));
                    setHasMore(mecanicos.length + initialLimit < parseInt(totalCount, 10));
                } else {
                    setHasMore(false);
                }
                return response.json();
            })
            .then(newMecanicos => {
                setMecanicos(prevMecanicos => [...prevMecanicos, ...newMecanicos]);
                setLoading(false);
            })
            .catch(error => {
                console.error('Erro ao buscar mecânicos:', error);
                setLoading(false);
            });
    }, [offset]);

    const removerMecanico = async (id) => {
        if (window.confirm(`Tem certeza que deseja remover o mecânico com ID ${id}?`)) {
            try {
                const response = await fetch(`/mecanicos/remover/${id}`, {
                    method: 'DELETE',
                });

                if (response.ok) {
                    console.log(`Mecânico com ID ${id} removido com sucesso.`);
                    setMecanicos([]);
                    setOffset(initialOffset);
                    setHasMore(true);
                } else if (response.status === 404) {
                    setErroRemover(`Mecânico com ID ${id} não encontrado.`);
                } else {
                    console.error(`Erro ao remover mecânico com ID ${id}:`, response.status);
                    const text = await response.text();
                    console.error('Corpo da resposta de erro:', text);
                    setErroRemover(`Erro ao remover mecânico com ID ${id}.`);
                }
            } catch (error) {
                console.error('Erro ao fazer a requisição de remoção:', error);
                setErroRemover(`Erro ao remover mecânico com ID ${id}.`);
            }
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.conteudoPrincipal}>
              

                <div className={styles.comissoesContainer}>
                    <ComissoesMecanicos onComissoesFetched={abrirModalComissoes} />
                </div>
            </div>
            <div className={styles.gridDireita}>
    

                {/* MODAL DE CADASTRO DE MECÂNICOS */}
                <Modal
                    isOpen={isCadastroVisivel}
                    onRequestClose={handleFecharCadastro}
                    className={styles.modal}
                    overlayClassName={styles.overlay}
                    contentLabel="Cadastrar Novo Mecânico"
                >
                    <div className={styles.modalContent}>
                        <span className={styles.closeButton} onClick={handleFecharCadastro}>
                            &times;
                        </span>
                       
                        <MecanicoForm
                            onMecanicoCadastrado={handleMecanicoCadastrado}
                            onClose={handleFecharCadastro}
                        />
                    </div>
                </Modal>

                {/* MODAL DE COMISSÕES */}
                <Modal
                    isOpen={isComissoesModalVisivel}
                    onRequestClose={fecharModalComissoes}
                    className={styles.modal}
                    overlayClassName={styles.overlay}
                    contentLabel="Comissões por Período"
                >
                    <div className={styles.modalContent}>
                        <span className={styles.closeButton} onClick={fecharModalComissoes}>
                            &times;
                        </span>
                        <h2>Comissões de {dadosComissoes.length > 0 ? `${dadosComissoes[0]?.mes}/${dadosComissoes[0]?.ano}` : 'Período'}</h2>
                        {erroComissoes && <p className={styles.erro}>{erroComissoes}</p>}
                        {dadosComissoes.length > 0 ? (
                            <table className={styles.comissoesTable}>
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Nome do Mecânico</th>
                                        <th>Comissão</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {dadosComissoes.map((comissao) => (
                                        <tr key={comissao.mecanicoId}>
                                            <td>{comissao.mecanicoId}</td>
                                            <td>{comissao.nomeMecanico}</td>
                                            <td>R$ {comissao.comissao?.toFixed(2)}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        ) : (
                            !erroComissoes && <p className={styles.noComissoes}>Nenhuma comissão encontrada para o período.</p>
                        )}
                    </div>
                </Modal>

                <h4>Mecânicos Cadastrados</h4>
                <ol className={styles.ListaMecanico}>
                    {mecanicos.map((mecanico, index) => (
                        <React.Fragment key={mecanico.id}>
                            <li ref={index === mecanicos.length - 1 ? lastMecanicoElementRef : null}>
                                {mecanico.nome}
                                <button onClick={() => removerMecanico(mecanico.id)} className={styles.botaoRemover}>Remover</button>
                            </li>
                        </React.Fragment>
                    ))}
              
                </ol>
                <button className={styles.btnCadastroMecanico} onClick={handleAbrirCadastro}>Novo Mecânico</button>
                {erroRemover && <p className={styles.erro}>{erroRemover}</p>}
            </div>
        </div>
    );
}

export default ControleMecanicos;