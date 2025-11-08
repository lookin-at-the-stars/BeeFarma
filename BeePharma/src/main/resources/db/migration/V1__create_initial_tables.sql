-- Criação das tabelas iniciais
CREATE TABLE produtos (
    id CHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(1000),
    codigo_anvisa VARCHAR(255) UNIQUE,
    unidade VARCHAR(255) NOT NULL,
    classe_terapeutica VARCHAR(255),
    criado_em DATE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE usuarios (
    id CHAR(36) PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE lotes (
    id CHAR(36) PRIMARY KEY,
    produto_id CHAR(36) NOT NULL,
    numero_lote VARCHAR(255) NOT NULL UNIQUE,
    data_fabricacao DATE NOT NULL,
    data_validade DATE NOT NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE ordens_producao (
    id CHAR(36) PRIMARY KEY,
    numero_op VARCHAR(255) NOT NULL UNIQUE,
    produto_id CHAR(36) NOT NULL,
    quantidade_planejada DECIMAL(19,4) NOT NULL,
    data_inicio DATE,
    data_fim_prevista DATE,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE inventario_itens (
    id CHAR(36) PRIMARY KEY,
    produto_id CHAR(36) NOT NULL,
    lote_id CHAR(36) NOT NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE movimentos_estoque (
    id CHAR(36) PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    produto_id CHAR(36) NOT NULL,
    lote_id CHAR(36) NOT NULL,
    quantidade DECIMAL(19,4) NOT NULL,
    data_hora DATETIME NOT NULL,
    responsavel_id CHAR(36) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (responsavel_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;