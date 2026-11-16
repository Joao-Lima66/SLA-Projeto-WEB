-- Banco de dados: Sistema de Localizacao Academica (SLA)
-- Spring Boot + JDBC puro (sem JPA/Hibernate)

DROP DATABASE IF EXISTS sla_db;
CREATE DATABASE sla_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sla_db;

-- Tabela: perfis
CREATE TABLE perfis (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabela: usuarios
CREATE TABLE usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    matricula VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_usuarios_perfil FOREIGN KEY (perfil_id) REFERENCES perfis(id)
);

-- Tabela: salas
CREATE TABLE salas (
    id INT NOT NULL AUTO_INCREMENT,
    numero VARCHAR(20) NOT NULL,
    bloco VARCHAR(10) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    capacidade INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

-- Tabela: disciplinas
CREATE TABLE disciplinas (
    id INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(20) NOT NULL,
    professor_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_disciplinas_professor FOREIGN KEY (professor_id) REFERENCES usuarios(id)
);

-- Tabela: alocacoes
CREATE TABLE alocacoes (
    id INT NOT NULL AUTO_INCREMENT,
    sala_id INT NOT NULL,
    disciplina_id INT NOT NULL,
    usuario_id INT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL DEFAULT 'SEGUNDA',
    horario VARCHAR(20) NOT NULL,
    ativa TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    CONSTRAINT fk_alocacoes_sala FOREIGN KEY (sala_id) REFERENCES salas(id),
    CONSTRAINT fk_alocacoes_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplinas(id),
    CONSTRAINT fk_alocacoes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Dados iniciais: perfis
INSERT INTO perfis (nome) VALUES
('Aluno'),
('Professor'),
('Coordenadora');

-- Dados iniciais: usuarios (2 alunos, 2 professores, 1 coordenadora)
INSERT INTO usuarios (nome, matricula, email, senha, perfil_id) VALUES
('Ana Silva', '2023001', 'ana@sla.edu', '123456', 1),
('Carlos Mendes', '2023002', 'carlos@sla.edu', '123456', 1),
('Maria Souza', 'P001', 'maria@sla.edu', '123456', 2),
('Joao Pereira', 'P002', 'joao@sla.edu', '123456', 2),
('Fernanda Costa', 'C001', 'fernanda@sla.edu', '123456', 3);

-- Dados iniciais: salas (teoricas e laboratorios)
INSERT INTO salas (numero, bloco, tipo, capacidade, status) VALUES
('101', 'A', 'TEORICA', 40, 'DISPONIVEL'),
('102', 'A', 'TEORICA', 35, 'DISPONIVEL'),
('LAB01', 'B', 'LABORATORIO', 20, 'DISPONIVEL'),
('LAB02', 'B', 'LABORATORIO', 15, 'DISPONIVEL');

-- Dados iniciais: disciplinas (vinculadas aos professores)
INSERT INTO disciplinas (nome, codigo, professor_id) VALUES
('Matematica', 'MAT101', 3),
('Fisica', 'FIS101', 4),
('Programacao', 'PRO101', 3);

-- Dados iniciais: alocacoes
INSERT INTO alocacoes (sala_id, disciplina_id, usuario_id, dia_semana, horario, ativa) VALUES
(1, 1, 1, 'SEGUNDA', '08:00', 1),
(3, 3, 2, 'TERCA', '14:00', 1),
(2, 2, 2, 'QUARTA', '10:00', 1);
