
import React, { useState } from 'react';

function MecanicoForm({ onMecanicoCadastrado, onClose }) {
  const [nome, setNome] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch('/mecanicos/cadastrar', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nome }),
      });

      if (response.ok) {
        console.log('Mecânico cadastrado com sucesso!');
        setNome(''); 
        if (onMecanicoCadastrado) {
          onMecanicoCadastrado(); 
        }
        if (onClose) {
          onClose();
        }
      } else {
        console.error('Erro ao cadastrar mecânico.');
      }
    } catch (error) {
      console.error('Erro na requisição:', error);
    }
  };

  return (
    <div>
      <h3>Cadastrar Novo Mecânico</h3>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="nome">Nome:</label>
          <input
            type="text"
            id="nome"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />
        </div>
        <button type="submit">Salvar Mecânico</button>
        <button type="button" onClick={onClose}>Cancelar</button>
      </form>
    </div>
  );
}

export default MecanicoForm;