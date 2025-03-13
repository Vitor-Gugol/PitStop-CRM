-- Criando o banco de dados
CREATE DATABASE crm_mecanica;
\c crm_mecanica;

-- Tabela Cliente
CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(50) UNIQUE,
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Carro
CREATE TABLE carro (
    id_carro SERIAL PRIMARY KEY,
    id_cliente INT REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INT CHECK (ano >= 1900 AND ano <= EXTRACT(YEAR FROM CURRENT_DATE)),
    placa VARCHAR(10) UNIQUE NOT NULL,
    chassi VARCHAR(50) UNIQUE NOT NULL,
    cor VARCHAR(30)
);

-- Tabela Peça
CREATE TABLE peca (
    id_peca SERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    quantidade_estoque INT DEFAULT 0 CHECK (quantidade_estoque >= 0)
);

-- Tabela Ordem de Serviço (OS)
CREATE TABLE ordem_servico (
    id_os SERIAL PRIMARY KEY,
    id_cliente INT REFERENCES cliente(id_cliente) ON DELETE SET NULL,
    id_carro INT REFERENCES carro(id_carro) ON DELETE CASCADE,
    data_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_prevista_saida TIMESTAMP,
    status VARCHAR(50) CHECK (status IN ('Em andamento', 'Concluído', 'Cancelado')) DEFAULT 'Em andamento',
    valor_total DECIMAL(10,2) DEFAULT 0 CHECK (valor_total >= 0)
);

-- Tabela Serviço
CREATE TABLE servico (
    id_servico SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0)
);

-- Tabela Peças Utilizadas
CREATE TABLE peca_utilizada (
    id_peca_utilizada SERIAL PRIMARY KEY,
    id_os INT REFERENCES ordem_servico(id_os) ON DELETE CASCADE,
    id_peca INT REFERENCES peca(id_peca) ON DELETE CASCADE,
    quantidade INT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0)
);

-- Tabela Serviços Realizados
CREATE TABLE servico_realizado (
    id_servico_realizado SERIAL PRIMARY KEY,
    id_os INT REFERENCES ordem_servico(id_os) ON DELETE CASCADE,
    id_servico INT REFERENCES servico(id_servico) ON DELETE CASCADE,
    preco_cobrado DECIMAL(10,2) NOT NULL CHECK (preco_cobrado >= 0)
);
