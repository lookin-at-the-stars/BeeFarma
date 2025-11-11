# Testes Práticos - BeePharma API

## Configuração
Base URL: `http://localhost:8080`

## 1. Teste de Aprovação de Lotes

### 1.1 Criar um Produto (pré-requisito)
```bash
curl -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Paracetamol 500mg",
    "descricao": "Analgésico e antipirético",
    "principioAtivo": "Paracetamol",
    "codigoAnvisa": "12345678901234"
  }'
```

**Guardar o `id` retornado para usar nos próximos passos**

### 1.2 Criar um Lote em Quarentena
```bash
curl -X POST http://localhost:8080/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-01",
    "dataValidade": "2025-11-01",
    "quantidade": 1000.00
  }'
```

**Guardar o `id` do lote retornado**

### 1.3 Listar Lotes em Quarentena
```bash
curl -X GET http://localhost:8080/lotes/status/QUARENTENA
```

### 1.4 Aprovar o Lote
```bash
curl -X POST http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE/aprovar
```

**Resultado esperado:** Status muda de QUARENTENA para APROVADO

### 1.5 Listar Lotes Aprovados
```bash
curl -X GET http://localhost:8080/lotes/status/APROVADO
```

---

## 2. Teste de Reprovação de Lotes

### 2.1 Criar outro Lote
```bash
curl -X POST http://localhost:8080/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-002",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-05",
    "dataValidade": "2025-11-05",
    "quantidade": 500.00
  }'
```

### 2.2 Reprovar o Lote
```bash
curl -X POST "http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE/reprovar?motivo=Contaminacao+detectada+no+teste+de+qualidade"
```

**Resultado esperado:** Status muda de QUARENTENA para REPROVADO

### 2.3 Listar Lotes Reprovados
```bash
curl -X GET http://localhost:8080/lotes/status/REPROVADO
```

---

## 3. Teste de Edição de Produtos

### 3.1 Editar um Produto Existente
```bash
curl -X PUT http://localhost:8080/produtos/COLE_AQUI_O_ID_DO_PRODUTO \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Paracetamol 500mg - Atualizado",
    "descricao": "Analgésico e antipirético de uso oral",
    "principioAtivo": "Paracetamol",
    "codigoAnvisa": "12345678901234"
  }'
```

**Resultado esperado:** Produto atualizado com sucesso

### 3.2 Tentar editar com nome vazio (deve falhar)
```bash
curl -X PUT http://localhost:8080/produtos/COLE_AQUI_O_ID_DO_PRODUTO \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "",
    "descricao": "Teste",
    "principioAtivo": "Paracetamol",
    "codigoAnvisa": "12345678901234"
  }'
```

**Resultado esperado:** Erro 400 - "Nome do produto é obrigatório"

---

## 4. Teste de Edição de Lotes

### 4.1 Criar um Lote para Edição
```bash
curl -X POST http://localhost:8080/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-003",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-10",
    "dataValidade": "2025-11-10",
    "quantidade": 750.00
  }'
```

### 4.2 Editar o Lote (ainda em QUARENTENA)
```bash
curl -X PUT http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-003",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-10",
    "dataValidade": "2025-11-10",
    "quantidade": 900.00
  }'
```

**Resultado esperado:** Lote atualizado com nova quantidade

### 4.3 Aprovar o Lote
```bash
curl -X POST http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE/aprovar
```

### 4.4 Tentar Editar o Lote Aprovado (deve falhar)
```bash
curl -X PUT http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-003",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-10",
    "dataValidade": "2025-11-10",
    "quantidade": 1000.00
  }'
```

**Resultado esperado:** Erro 400 - "Não é possível editar um lote aprovado"

---

## 5. Teste de Edição de Ordens de Produção

### 5.1 Criar uma Ordem de Produção
```bash
curl -X POST http://localhost:8080/ordens-producao \
  -H "Content-Type: application/json" \
  -d '{
    "numeroOP": "OP-2024-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "quantidadePlanejada": 2000.00,
    "dataInicio": "2024-11-15",
    "dataFimPrevista": "2024-12-15"
  }'
```

**Guardar o `id` da OP retornado**

### 5.2 Editar a OP (ainda PLANEJADA)
```bash
curl -X PUT http://localhost:8080/ordens-producao/COLE_AQUI_O_ID_DA_OP \
  -H "Content-Type: application/json" \
  -d '{
    "numeroOP": "OP-2024-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "quantidadePlanejada": 2500.00,
    "dataInicio": "2024-11-15",
    "dataFimPrevista": "2024-12-15"
  }'
```

**Resultado esperado:** OP atualizada com nova quantidade

### 5.3 Mudar Status para EM_ANDAMENTO
```bash
curl -X PATCH "http://localhost:8080/ordens-producao/COLE_AQUI_O_ID_DA_OP/status?status=EM_ANDAMENTO"
```

### 5.4 Tentar Mudar o Número da OP (deve falhar)
```bash
curl -X PUT http://localhost:8080/ordens-producao/COLE_AQUI_O_ID_DA_OP \
  -H "Content-Type: application/json" \
  -d '{
    "numeroOP": "OP-2024-999",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "quantidadePlanejada": 2500.00,
    "dataInicio": "2024-11-15",
    "dataFimPrevista": "2024-12-15"
  }'
```

**Resultado esperado:** Erro 400 - "Não é possível alterar o número de uma OP em andamento"

### 5.5 Mudar Status para CONCLUIDA
```bash
curl -X PATCH "http://localhost:8080/ordens-producao/COLE_AQUI_O_ID_DA_OP/status?status=CONCLUIDA"
```

### 5.6 Tentar Editar OP Concluída (deve falhar)
```bash
curl -X PUT http://localhost:8080/ordens-producao/COLE_AQUI_O_ID_DA_OP \
  -H "Content-Type: application/json" \
  -d '{
    "numeroOP": "OP-2024-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "quantidadePlanejada": 3000.00,
    "dataInicio": "2024-11-15",
    "dataFimPrevista": "2024-12-15"
  }'
```

**Resultado esperado:** Erro 400 - "Não é possível editar uma ordem de produção concluída"

---

## 6. Testes de Validação

### 6.1 Tentar Aprovar Lote com Data Vencida
```bash
# Primeiro criar um lote com data vencida
curl -X POST http://localhost:8080/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-VENCIDO-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2023-01-01",
    "dataValidade": "2024-01-01",
    "quantidade": 100.00
  }'

# Tentar aprovar
curl -X POST http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE/aprovar
```

**Resultado esperado:** Erro 400 - "Não é possível aprovar um lote com data de validade vencida"

### 6.2 Tentar Criar Lote com Quantidade Negativa
```bash
curl -X POST http://localhost:8080/lotes \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-INVALIDO-001",
    "produtoId": "COLE_AQUI_O_ID_DO_PRODUTO",
    "dataFabricacao": "2024-11-01",
    "dataValidade": "2025-11-01",
    "quantidade": -100.00
  }'
```

**Resultado esperado:** Erro 400 - "Quantidade deve ser maior que zero"

### 6.3 Tentar Reprovar Lote Já Aprovado
```bash
# Usar um ID de lote que já foi aprovado anteriormente
curl -X POST "http://localhost:8080/lotes/COLE_AQUI_O_ID_DO_LOTE_APROVADO/reprovar?motivo=Teste"
```

**Resultado esperado:** Erro 400 - "Não é possível reprovar um lote já aprovado"

---

## Checklist de Testes

- [ ] Criar produto
- [ ] Criar lote em quarentena
- [ ] Aprovar lote
- [ ] Criar outro lote
- [ ] Reprovar lote
- [ ] Editar produto
- [ ] Editar lote em quarentena
- [ ] Tentar editar lote aprovado (deve falhar)
- [ ] Criar ordem de produção
- [ ] Editar OP planejada
- [ ] Mudar OP para em andamento
- [ ] Tentar mudar número da OP em andamento (deve falhar)
- [ ] Mudar OP para concluída
- [ ] Tentar editar OP concluída (deve falhar)
- [ ] Validar data de validade na aprovação
- [ ] Validar quantidade negativa
- [ ] Validar reprovação de lote aprovado

---

## Observações

1. Substitua `COLE_AQUI_O_ID_DO_PRODUTO`, `COLE_AQUI_O_ID_DO_LOTE` e `COLE_AQUI_O_ID_DA_OP` pelos IDs reais retornados nas respostas das APIs.

2. Para visualizar as respostas formatadas, você pode adicionar `| jq` no final dos comandos curl (requer ter o jq instalado).

3. Para testes em ferramentas como Postman ou Insomnia, importe esta documentação e ajuste os endpoints conforme necessário.

4. Todos os testes assumem que o servidor está rodando em `http://localhost:8080`.
