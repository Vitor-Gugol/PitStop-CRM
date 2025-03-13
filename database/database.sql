CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(15),
    email VARCHAR(100),
    endereco TEXT,
    data_cadastro DATE NOT NULL DEFAULT CURRENT_DATE
);


CREATE TABLE carro (
    id_carro SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    ano INT,
    placa VARCHAR(10),
    chassi VARCHAR(20),
    cor VARCHAR(30),
    CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
);


CREATE TABLE peca (
    id_peca SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0
);


CREATE TABLE ordem_servico (
    id_os SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_carro INT NOT NULL,
    data_entrada DATE NOT NULL DEFAULT CURRENT_DATE,
    data_prevista_saida DATE,
    status VARCHAR(20) DEFAULT 'Em andamento',
    valor_total DECIMAL(10, 2),
    CONSTRAINT fk_os_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente),
    CONSTRAINT fk_os_carro FOREIGN KEY (id_carro) REFERENCES carro (id_carro)
);


CREATE TABLE servico (
    id_servico SERIAL PRIMARY KEY,
    descricao VARCHAR(200) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL
);



CREATE TABLE pecas_utilizadas (
    id_peca_utilizada SERIAL PRIMARY KEY,
    id_os INT NOT NULL,
    id_peca INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_peca_os FOREIGN KEY (id_os) REFERENCES ordem_servico (id_os),
    CONSTRAINT fk_peca FOREIGN KEY (id_peca) REFERENCES peca (id_peca)
);


CREATE TABLE mecanico (
    id_mecanico SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100),
    telefone VARCHAR(15)
);


