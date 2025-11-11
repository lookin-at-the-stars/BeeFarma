# Documentação dos Endpoints - BeePharma

## Funcionalidades Implementadas

### 1. Aprovação e Reprovação de Lotes

#### 1.1 Aprovar Lote
**Endpoint:** `POST /lotes/{id}/aprovar`

**Descrição:** Aprova um lote que está em quarentena, mudando seu status para APROVADO.

**Validações:**
- O lote não pode estar já aprovado
- O lote não pode estar vencido
- A data de validade não pode estar vencida
- Apenas lotes em QUARENTENA podem ser aprovados

**Exemplo de uso:**
```bash
curl -X POST http://localhost:8080/lotes/{id}/aprovar
```

**Resposta de sucesso (200 OK):**
```json
{
  "id": "uuid-do-lote",
  "numeroLote": "LOTE-2024-001",
  "produtoId": "uuid-do-produto",
  "dataFabricacao": "2024-01-15",
  "dataValidade": "2025-01-15",
  "quantidade": 1000.00,
  "status": "APROVADO"
}
```

**Erros possíveis:**
- 404: Lote não encontrado
- 400: Lote já está aprovado
- 400: Não é possível aprovar um lote vencido
- 400: Não é possível aprovar um lote com data de validade vencida

---

#### 1.2 Reprovar Lote
**Endpoint:** `POST /lotes/{id}/reprovar?motivo={motivo}`

**Descrição:** Reprova um lote que está em quarentena, mudando seu status para REPROVADO.

**Parâmetros:**
- `motivo` (opcional): Motivo da reprovação

**Validações:**
- O lote não pode estar já reprovado
- Não é possível reprovar um lote já aprovado (deve criar novo lote)

**Exemplo de uso:**
```bash
curl -X POST "http://localhost:8080/lotes/{id}/reprovar?motivo=Contaminacao+detectada"
```

**Resposta de sucesso (200 OK):**
```json
{
  "id": "uuid-do-lote",
  "numeroLote": "LOTE-2024-001",
  "produtoId": "uuid-do-produto",
  "dataFabricacao": "2024-01-15",
  "dataValidade": "2025-01-15",
  "quantidade": 1000.00,
  "status": "REPROVADO"
}
```

**Erros possíveis:**
- 404: Lote não encontrado
- 400: Lote já está reprovado
- 400: Não é possível reprovar um lote já aprovado

---

### 2. Edição de Produtos

#### 2.1 Editar Produto
**Endpoint:** `PUT /produtos/{id}`

**Descrição:** Atualiza os dados de um produto existente.

**Validações implementadas:**
- Nome do produto é obrigatório
- Descrição não pode exceder 1000 caracteres
- Princípio ativo é obrigatório
- Código ANVISA não pode ser duplicado

**Exemplo de uso:**
```bash
curl -X PUT http://localhost:8080/produtos/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Produto Atualizado",
    "descricao": "Nova descrição do produto",
    "principioAtivo": "Princípio Ativo",
    "codigoAnvisa": "12345678901234"
  }'
```

**Resposta de sucesso (200 OK):**
```json
{
  "id": "uuid-do-produto",
  "nome": "Produto Atualizado",
  "descricao": "Nova descrição do produto",
  "principioAtivo": "Princípio Ativo",
  "codigoAnvisa": "12345678901234",
  "criadoEm": "2024-01-15T10:30:00"
}
```

**Erros possíveis:**
- 404: Produto não encontrado
- 400: Nome do produto é obrigatório
- 400: Descrição do produto não pode exceder 1000 caracteres
- 400: Princípio ativo é obrigatório
- 400: Código ANVISA já cadastrado

---

### 3. Edição de Lotes

#### 3.1 Editar Lote
**Endpoint:** `PUT /lotes/{id}`

**Descrição:** Atualiza os dados de um lote existente.

**Validações implementadas:**
- Número do lote é obrigatório
- Quantidade deve ser maior que zero
- Data de fabricação não pode ser posterior à data de validade
- Data de fabricação não pode ser futura
- Data de validade não pode estar vencida
- **Não é possível editar lotes com status: APROVADO, REPROVADO ou VENCIDO**
- Apenas lotes em QUARENTENA podem ser editados

**Exemplo de uso:**
```bash
curl -X PUT http://localhost:8080/lotes/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "numeroLote": "LOTE-2024-001",
    "produtoId": "uuid-do-produto",
    "dataFabricacao": "2024-01-15",
    "dataValidade": "2025-01-15",
    "quantidade": 1500.00
  }'
```

**Resposta de sucesso (200 OK):**
```json
{
  "id": "uuid-do-lote",
  "numeroLote": "LOTE-2024-001",
  "produtoId": "uuid-do-produto",
  "dataFabricacao": "2024-01-15",
  "dataValidade": "2025-01-15",
  "quantidade": 1500.00,
  "status": "QUARENTENA"
}
```

**Erros possíveis:**
- 404: Lote não encontrado
- 400: Não é possível editar um lote aprovado
- 400: Não é possível editar um lote reprovado
- 400: Não é possível editar um lote vencido
- 400: Número do lote é obrigatório
- 400: Quantidade deve ser maior que zero
- 400: Data de fabricação não pode ser posterior à data de validade
- 400: Data de fabricação não pode ser futura
- 400: Data de validade não pode estar vencida
- 400: Número de lote já cadastrado

---

### 4. Edição de Ordens de Produção

#### 4.1 Editar Ordem de Produção
**Endpoint:** `PUT /ordens-producao/{id}`

**Descrição:** Atualiza os dados de uma ordem de produção existente.

**Validações implementadas:**
- Número da OP é obrigatório
- Quantidade planejada deve ser maior que zero
- Data de início não pode ser posterior à data de fim prevista
- **Não é possível editar OPs com status: CONCLUIDA ou CANCELADA**
- **OPs EM_ANDAMENTO não podem ter o número alterado**
- Apenas OPs PLANEJADAS podem ser editadas livremente

**Exemplo de uso:**
```bash
curl -X PUT http://localhost:8080/ordens-producao/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "numeroOP": "OP-2024-001",
    "produtoId": "uuid-do-produto",
    "quantidadePlanejada": 2000.00,
    "dataInicio": "2024-01-20",
    "dataFimPrevista": "2024-02-20"
  }'
```

**Resposta de sucesso (200 OK):**
```json
{
  "id": "uuid-da-op",
  "numeroOP": "OP-2024-001",
  "produtoId": "uuid-do-produto",
  "quantidadePlanejada": 2000.00,
  "dataInicio": "2024-01-20",
  "dataFimPrevista": "2024-02-20",
  "status": "PLANEJADA"
}
```

**Erros possíveis:**
- 404: Ordem de Produção não encontrada
- 400: Não é possível editar uma ordem de produção concluída
- 400: Não é possível editar uma ordem de produção cancelada
- 400: Não é possível alterar o número de uma OP em andamento
- 400: Número da OP é obrigatório
- 400: Quantidade planejada deve ser maior que zero
- 400: Data de início não pode ser posterior à data de fim prevista
- 400: Número de OP já cadastrado

---

## Fluxo de Trabalho Recomendado

### Para Lotes:

1. **Criar Lote** → Status: QUARENTENA
2. **Realizar testes de qualidade** (fora do sistema)
3. **Aprovar Lote** → Status: APROVADO (se passou nos testes)
   - OU **Reprovar Lote** → Status: REPROVADO (se falhou nos testes)
4. **Usar lote aprovado** nas operações de estoque

### Regras de Edição:

- **Produtos**: Podem ser editados a qualquer momento
- **Lotes**: Apenas em QUARENTENA
- **Ordens de Produção**: 
  - PLANEJADA: Edição completa
  - EM_ANDAMENTO: Edição limitada (não pode mudar número)
  - CONCLUIDA/CANCELADA: Não pode editar

---

## Status Disponíveis

### Lotes:
- `QUARENTENA`: Lote recém-criado, aguardando aprovação
- `APROVADO`: Lote aprovado e liberado para uso
- `REPROVADO`: Lote reprovado, não pode ser usado
- `VENCIDO`: Lote com data de validade vencida

### Ordens de Produção:
- `PLANEJADA`: OP criada, aguardando início
- `EM_ANDAMENTO`: OP em execução
- `CONCLUIDA`: OP finalizada
- `CANCELADA`: OP cancelada

---

## Notas Importantes

1. **Lotes aprovados** não podem ser editados ou reprovados posteriormente. Se houver problema com um lote aprovado, deve-se criar um novo lote ou registrar movimentação de estoque.

2. **Datas de validade** são verificadas automaticamente na aprovação do lote.

3. **Códigos únicos** (número de lote, número de OP, código ANVISA) são validados para evitar duplicações.

4. **Status de transição** seguem regras de negócio específicas para garantir a integridade dos dados.

5. Todos os endpoints retornam **mensagens de erro descritivas** para facilitar a identificação e correção de problemas.
