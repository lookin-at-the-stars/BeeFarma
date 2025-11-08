# 🧪 Guia de Testes - BeePharma API

Base URL: `http://localhost:8080/api`

## 📋 Índice de Endpoints

### 1. Home
- `GET /` - Página inicial

### 2. Produtos (`/produtos`)
- `GET /produtos` - Listar todos
- `GET /produtos/{id}` - Buscar por ID
- `POST /produtos` - Criar novo
- `PUT /produtos/{id}` - Atualizar
- `DELETE /produtos/{id}` - Excluir

### 3. Lotes (`/lotes`)
- `GET /lotes` - Listar todos
- `GET /lotes/status/{status}` - Listar por status
- `GET /lotes/{id}` - Buscar por ID
- `POST /lotes` - Criar novo
- `PUT /lotes/{id}` - Atualizar
- `PATCH /lotes/{id}/status` - Atualizar status

### 4. Movimentos de Estoque (`/movimentos-estoque`)
- `GET /movimentos-estoque` - Listar todos
- `GET /movimentos-estoque/tipo/{tipo}` - Listar por tipo
- `GET /movimentos-estoque/produto/{produtoId}` - Listar por produto
- `GET /movimentos-estoque/periodo` - Listar por período
- `GET /movimentos-estoque/{id}` - Buscar por ID
- `POST /movimentos-estoque` - Registrar movimento

### 5. Inventário (`/inventario`)
- `GET /inventario` - Listar todos
- `GET /inventario/produto/{produtoId}` - Listar por produto
- `GET /inventario/localizacao/{localizacao}` - Listar por localização
- `GET /inventario/{id}` - Buscar por ID
- `POST /inventario` - Criar item
- `PUT /inventario/{id}` - Atualizar item
- `DELETE /inventario/{id}` - Excluir item

### 6. Ordens de Produção (`/ordens-producao`)
- `GET /ordens-producao` - Listar todas
- `GET /ordens-producao/status/{status}` - Listar por status
- `GET /ordens-producao/{id}` - Buscar por ID
- `POST /ordens-producao` - Criar nova
- `PUT /ordens-producao/{id}` - Atualizar
- `PATCH /ordens-producao/{id}/status` - Atualizar status

---

## 🧪 Exemplos de Testes (PowerShell)

### 1️⃣ Testar Home
```powershell
curl http://localhost:8080/api/
```

### 2️⃣ PRODUTOS

#### Criar um produto
```powershell
$body = @{
    nome = "Paracetamol 500mg"
    descricao = "Analgésico e antitérmico"
    codigoAnvisa = "1234567890123"
    fabricante = "Laboratório Exemplo"
    principioAtivo = "Paracetamol"
    concentracao = "500mg"
    formaFarmaceutica = "Comprimido"
    unidadeMedida = "COMPRIMIDO"
    estoque = 1000
    estoqueMinimo = 100
    estoqueMaximo = 5000
    preco = 15.50
    ativo = $true
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/produtos `
  -H "Content-Type: application/json" `
  -d $body
```

#### Listar todos os produtos
```powershell
curl http://localhost:8080/api/produtos
```

#### Buscar produto por ID
```powershell
# Substitua {id} pelo UUID retornado ao criar o produto
curl http://localhost:8080/api/produtos/{id}
```

#### Atualizar produto
```powershell
$body = @{
    nome = "Paracetamol 750mg"
    descricao = "Analgésico e antitérmico - dose reforçada"
    codigoAnvisa = "1234567890123"
    fabricante = "Laboratório Exemplo"
    principioAtivo = "Paracetamol"
    concentracao = "750mg"
    formaFarmaceutica = "Comprimido"
    unidadeMedida = "COMPRIMIDO"
    estoque = 1000
    estoqueMinimo = 100
    estoqueMaximo = 5000
    preco = 18.50
    ativo = $true
} | ConvertTo-Json

curl -X PUT http://localhost:8080/api/produtos/{id} `
  -H "Content-Type: application/json" `
  -d $body
```

#### Excluir produto
```powershell
curl -X DELETE http://localhost:8080/api/produtos/{id}
```

### 3️⃣ LOTES

#### Criar um lote
```powershell
# Primeiro, pegue o ID de um produto existente
$body = @{
    numeroLote = "LOTE2024001"
    produtoId = "{produto-uuid-aqui}"
    dataFabricacao = "2024-01-15"
    dataValidade = "2026-01-15"
    quantidade = 1000
    fornecedor = "Fornecedor ABC"
    numeroNotaFiscal = "NF-2024-001"
    observacoes = "Lote em quarentena para análise"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/lotes `
  -H "Content-Type: application/json" `
  -d $body
```

#### Listar todos os lotes
```powershell
curl http://localhost:8080/api/lotes
```

#### Listar lotes por status
```powershell
# Status disponíveis: QUARENTENA, APROVADO, REPROVADO, EM_USO, ESGOTADO
curl http://localhost:8080/api/lotes/status/QUARENTENA
```

#### Atualizar status do lote
```powershell
curl -X PATCH "http://localhost:8080/api/lotes/{id}/status?status=APROVADO"
```

### 4️⃣ MOVIMENTOS DE ESTOQUE

#### Registrar entrada no estoque
```powershell
$body = @{
    tipo = "ENTRADA"
    produtoId = "{produto-uuid-aqui}"
    loteId = "{lote-uuid-aqui}"
    quantidade = 500
    motivo = "Compra de fornecedor"
    observacoes = "Recebimento conforme NF-2024-001"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/movimentos-estoque `
  -H "Content-Type: application/json" `
  -d $body
```

#### Registrar saída do estoque
```powershell
$body = @{
    tipo = "SAIDA"
    produtoId = "{produto-uuid-aqui}"
    loteId = "{lote-uuid-aqui}"
    quantidade = 100
    motivo = "Venda"
    observacoes = "Venda para Farmácia XYZ"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/movimentos-estoque `
  -H "Content-Type: application/json" `
  -d $body
```

#### Listar movimentos por período
```powershell
$inicio = "2024-01-01T00:00:00"
$fim = "2024-12-31T23:59:59"
curl "http://localhost:8080/api/movimentos-estoque/periodo?inicio=$inicio&fim=$fim"
```

#### Listar movimentos por tipo
```powershell
# Tipos: ENTRADA, SAIDA, AJUSTE, TRANSFERENCIA, DEVOLUCAO
curl http://localhost:8080/api/movimentos-estoque/tipo/ENTRADA
```

### 5️⃣ INVENTÁRIO

#### Criar item no inventário
```powershell
$body = @{
    produtoId = "{produto-uuid-aqui}"
    loteId = "{lote-uuid-aqui}"
    localizacao = "Prateleira A1"
    quantidadeFisica = 500
    quantidadeSistema = 500
    dataContagem = "2024-11-07T10:00:00"
    responsavel = "João Silva"
    observacoes = "Contagem regular mensal"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/inventario `
  -H "Content-Type: application/json" `
  -d $body
```

#### Listar itens por localização
```powershell
curl http://localhost:8080/api/inventario/localizacao/Prateleira%20A1
```

#### Listar itens por produto
```powershell
curl http://localhost:8080/api/inventario/produto/{produto-uuid}
```

### 6️⃣ ORDENS DE PRODUÇÃO

#### Criar ordem de produção
```powershell
$body = @{
    numeroOrdem = "OP-2024-001"
    produtoId = "{produto-uuid-aqui}"
    quantidadePlanejada = 1000
    dataPrevisaoInicio = "2024-11-10T08:00:00"
    dataPrevisaoFim = "2024-11-15T18:00:00"
    prioridade = "ALTA"
    observacoes = "Produção urgente para reposição de estoque"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/ordens-producao `
  -H "Content-Type: application/json" `
  -d $body
```

#### Listar ordens por status
```powershell
# Status: PLANEJADA, EM_ANDAMENTO, PAUSADA, CONCLUIDA, CANCELADA
curl http://localhost:8080/api/ordens-producao/status/PLANEJADA
```

#### Atualizar status da ordem
```powershell
curl -X PATCH "http://localhost:8080/api/ordens-producao/{id}/status?status=EM_ANDAMENTO"
```

---

## 🎯 Fluxo de Teste Completo

### Cenário: Cadastrar produto e registrar movimentação

```powershell
# 1. Criar um produto
$produtoBody = @{
    nome = "Ibuprofeno 600mg"
    descricao = "Anti-inflamatório"
    codigoAnvisa = "9876543210987"
    fabricante = "Pharma Labs"
    principioAtivo = "Ibuprofeno"
    concentracao = "600mg"
    formaFarmaceutica = "Comprimido"
    unidadeMedida = "COMPRIMIDO"
    estoque = 0
    estoqueMinimo = 50
    estoqueMaximo = 2000
    preco = 22.50
    ativo = $true
} | ConvertTo-Json

$produto = curl -X POST http://localhost:8080/api/produtos `
  -H "Content-Type: application/json" `
  -d $produtoBody | ConvertFrom-Json

Write-Host "Produto criado com ID: $($produto.id)"

# 2. Criar um lote
$loteBody = @{
    numeroLote = "LOTE2024002"
    produtoId = $produto.id
    dataFabricacao = "2024-11-01"
    dataValidade = "2026-11-01"
    quantidade = 1000
    fornecedor = "Fornecedor Premium"
    numeroNotaFiscal = "NF-2024-002"
    observacoes = "Lote prioritário"
} | ConvertTo-Json

$lote = curl -X POST http://localhost:8080/api/lotes `
  -H "Content-Type: application/json" `
  -d $loteBody | ConvertFrom-Json

Write-Host "Lote criado com ID: $($lote.id)"

# 3. Aprovar o lote
curl -X PATCH "http://localhost:8080/api/lotes/$($lote.id)/status?status=APROVADO"

# 4. Registrar entrada no estoque
$movimentoBody = @{
    tipo = "ENTRADA"
    produtoId = $produto.id
    loteId = $lote.id
    quantidade = 1000
    motivo = "Recebimento inicial"
    observacoes = "Primeiro recebimento do lote"
} | ConvertTo-Json

curl -X POST http://localhost:8080/api/movimentos-estoque `
  -H "Content-Type: application/json" `
  -d $movimentoBody

# 5. Verificar o produto atualizado
curl http://localhost:8080/api/produtos/$($produto.id)
```

---

## 📊 Swagger UI

Acesse a documentação interativa completa:
```
http://localhost:8080/api/swagger-ui.html
```

Ou a especificação OpenAPI:
```
http://localhost:8080/api/api-docs
```

---

## ⚠️ Observações Importantes

### Enums Disponíveis:

**UnidadeMedida:**
- COMPRIMIDO, CAPSULA, AMPOLA, FRASCO, CAIXA, TUBO, BISNAGA, ENVELOPE, SACHÊ

**LoteStatus:**
- QUARENTENA, APROVADO, REPROVADO, EM_USO, ESGOTADO

**MovimentoTipo:**
- ENTRADA, SAIDA, AJUSTE, TRANSFERENCIA, DEVOLUCAO

**OPStatus:**
- PLANEJADA, EM_ANDAMENTO, PAUSADA, CONCLUIDA, CANCELADA

**OPPrioridade:**
- BAIXA, MEDIA, ALTA, URGENTE

### Formato de Datas:
- Datas simples: `"2024-11-07"`
- Datas com hora: `"2024-11-07T10:00:00"`

### UUIDs:
- Todos os IDs são UUIDs no formato: `"550e8400-e29b-41d4-a716-446655440000"`
- Salve os IDs retornados pelas operações de criação para usar nas demais operações

---

## 🐛 Troubleshooting

### Erro de conexão:
```powershell
# Verificar se a aplicação está rodando
docker ps

# Ver logs do container
docker compose logs -f
```

### Reiniciar a aplicação:
```powershell
cd c:\Users\Gabriel\Documents\BeeFarma\BeePharma
docker compose restart
```

### Limpar banco de dados:
```powershell
docker compose down -v
docker compose up -d
```
