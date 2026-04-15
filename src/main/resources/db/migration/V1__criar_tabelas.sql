-- 1. Cria a tabela de Usuários primeiro (pois os planos vão depender dela)
CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          senha VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL DEFAULT 'PROFESSOR',
                          data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Cria a tabela com os dados inseridos pelo professor
CREATE TABLE dados_apoio (
                             id BIGSERIAL PRIMARY KEY,
                             nome VARCHAR(100) NOT NULL, -- Pode ser o nome do Aluno ou da Turma
                             materia VARCHAR(100) NOT NULL,
                             serie VARCHAR(100) NOT NULL,
                             conteudos VARCHAR(255) NOT NULL,
                             barreiras TEXT NOT NULL,
                             solucao_necessaria TEXT NOT NULL,
                             data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             id_usuario BIGINT REFERENCES usuarios(id)
);

-- 3. Cria a tabela do plano gerado, ligando com os dados e com o usuário
CREATE TABLE planos_apoio (
                              id BIGSERIAL PRIMARY KEY,
                              conteudo_gerado_ia TEXT,
                              id_dados BIGINT REFERENCES dados_apoio(id),
                              id_usuario BIGINT REFERENCES usuarios(id)
);