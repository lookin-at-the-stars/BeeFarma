-- Add default values for ID columns using MariaDB's native UUID() function
ALTER TABLE produtos 
    ALTER id SET DEFAULT UUID();

ALTER TABLE usuarios 
    ALTER id SET DEFAULT UUID();

ALTER TABLE lotes 
    ALTER id SET DEFAULT UUID();

ALTER TABLE ordens_producao 
    ALTER id SET DEFAULT UUID();

ALTER TABLE inventario_itens 
    ALTER id SET DEFAULT UUID();

ALTER TABLE movimentos_estoque 
    ALTER id SET DEFAULT UUID();