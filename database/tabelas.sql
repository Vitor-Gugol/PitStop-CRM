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


-- Insira esses dados 

INSERT INTO cliente (nome, telefone, email, endereco) VALUES
('Lucas Souza', '11987654321', 'lucas.souza@email.com', 'Rua Ametista, 123, São Paulo'),
('Juliana Costa', '21999887766', 'juliana.costa@email.com', 'Av. Central, 456, Rio de Janeiro'),
('Rafael Mendes', '31988556644', 'rafael.mendes@email.com', 'Rua Ouro Branco, 789, Belo Horizonte'),
('Patrícia Lima', '41977889900', 'patricia.lima@email.com', 'Rua das Rosas, 101, Curitiba'),
('Henrique Alves', '11933445577', 'henrique.alves@email.com', 'Av. Paulista, 1100, São Paulo');



INSERT INTO carro (id_cliente, marca, modelo, ano, placa, chassi, cor) VALUES
(1, 'Ford', 'Fiesta', 2017, 'AAA-1111', '1HGCM82633A111111', 'Vermelho'),
(2, 'Chevrolet', 'Cruze', 2020, 'BBB-2222', '2HGEJ6615WH200000', 'Azul'),
(3, 'Hyundai', 'HB20', 2019, 'CCC-3333', '3VWFE21C04M300000', 'Prata'),
(4, 'Volkswagen', 'Polo', 2021, 'DDD-4444', '9BWFE61JX24040000', 'Preto'),
(5, 'Toyota', 'Hilux', 2018, 'EEE-5555', 'JH4KA8260MC500000', 'Branco');


INSERT INTO peca (nome, descricao, preco, quantidade_estoque) VALUES
('Filtro de Combustível', 'Filtro específico para motores diesel', 75.00, 40),
('Pastilha de Freio', 'Pastilha para freios traseiros', 60.00, 50),
('Bateria Automotiva', 'Bateria 12V 70A', 350.00, 20),
('Correia Alternador', 'Correia específica para alternadores', 120.00, 30),
('Pneu Aro 16', 'Pneu de alta performance', 450.00, 15);

INSERT INTO servico (descricao, preco) VALUES
('Troca de Velas', 200.00),
('Revisão Geral', 800.00),
('Troca de Pneu', 100.00),
('Alinhamento Completo', 150.00),
('Balanceamento de Rodas', 120.00);

INSERT INTO ordem_servico (id_cliente, id_carro, data_prevista_saida, status, valor_total) VALUES
(1, 1, '2025-05-01 14:00:00', 'Em andamento', 0.00),
(2, 2, '2025-05-03 10:00:00', 'Concluído', 0.00),
(3, 3, '2025-05-05 16:00:00', 'Cancelado', 0.00),
(4, 4, '2025-05-07 11:30:00', 'Em andamento', 0.00),
(5, 5, '2025-05-10 09:00:00', 'Concluído', 0.00);

INSERT INTO peca_utilizada (id_os, id_peca, quantidade, preco_unitario) VALUES
(1, 1, 2, 75.00),
(1, 2, 4, 60.00),
(2, 3, 1, 350.00),
(4, 4, 3, 120.00),
(5, 5, 2, 450.00);

INSERT INTO servico_realizado (id_os, id_servico, preco_cobrado) VALUES
(1, 1, 200.00),
(2, 2, 800.00),
(4, 4, 150.00),
(5, 5, 120.00),
(5, 3, 100.00);


CREATE FUNCTION calcular_valor_total_os()
RETURNS TRIGGER AS $$
BEGIN
    NEW.valor_total = (
        SELECT COALESCE(SUM(peca_utilizada.quantidade * peca_utilizada.preco_unitario), 0)
        FROM peca_utilizada
        WHERE peca_utilizada.id_os = NEW.id_os
    ) + (
        SELECT COALESCE(SUM(servico_realizado.preco_cobrado), 0)
        FROM servico_realizado
        WHERE servico_realizado.id_os = NEW.id_os
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER atualizar_valor_total_os
AFTER INSERT OR UPDATE ON peca_utilizada
FOR EACH ROW
EXECUTE FUNCTION calcular_valor_total_os();




CREATE FUNCTION atualizar_estoque_pecas()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE peca
    SET quantidade_estoque = quantidade_estoque - NEW.quantidade
    WHERE id_peca = NEW.id_peca;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER atualizar_estoque
AFTER INSERT ON peca_utilizada
FOR EACH ROW
EXECUTE FUNCTION atualizar_estoque_pecas();



CREATE TABLE historico_status_os (
    id_historico SERIAL PRIMARY KEY,
    id_os INT REFERENCES ordem_servico(id_os) ON DELETE CASCADE,
    status_anterior VARCHAR(50),
    status_novo VARCHAR(50),
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_placa_carro ON carro(placa);
CREATE INDEX idx_nome_cliente ON cliente(nome);

CREATE OR REPLACE FUNCTION atualizar_estoque_pecas()
RETURNS TRIGGER AS $$
BEGIN
    -- Verificar se há estoque suficiente
    IF (SELECT quantidade_estoque FROM peca WHERE id_peca = NEW.id_peca) < NEW.quantidade THEN
        RAISE EXCEPTION 'Estoque insuficiente para a peça: ID %', NEW.id_peca;
    END IF;

    -- Atualizar estoque
    UPDATE peca
    SET quantidade_estoque = quantidade_estoque - NEW.quantidade
    WHERE id_peca = NEW.id_peca;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

