-- Adicionar coluna principio_ativo na tabela produtos
ALTER TABLE produtos 
ADD COLUMN principio_ativo VARCHAR(255);

-- Atualizar produtos existentes com um valor padrão baseado no nome
UPDATE produtos 
SET principio_ativo = nome 
WHERE principio_ativo IS NULL;

-- Tornar a coluna NOT NULL após popular os dados
ALTER TABLE produtos 
MODIFY COLUMN principio_ativo VARCHAR(255) NOT NULL;
