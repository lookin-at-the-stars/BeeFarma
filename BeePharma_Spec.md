# 🧬 Projeto BeePharma — Especificação Técnica (Monólito Spring Boot)

## 🎯 Objetivo
Sistema integrado para gestão da produção farmacêutica, controle de qualidade, estoque e rastreabilidade de lotes, em conformidade com normas da ANVISA/GMP.

---

## 🏗️ Arquitetura

**Stack técnica**
- Java 21 + Spring Boot 3.x (Web, JPA, Security, Validation)
- PostgreSQL (Flyway para migrations)
- Springdoc OpenAPI
- Docker/Docker Compose
- Lombok + MapStruct
- JWT + Spring Security
- JUnit + Testcontainers
- Thymeleaf ou React (frontend separado opcional)

**Estrutura do monólito (pacotes principais):**
```
br.com.beepharma
 ├── application (DTOs, services)
 ├── domain (entities, enums, repositories)
 ├── infrastructure (config, security, persistence)
 ├── presentation (controllers, views)
 └── util (mappers, exceptions)
```

---

## 🧱 Domínio principal (JPA Entities)

### Produto
```java
@Entity
class Produto {
  @Id UUID id;
  String nome;
  String descricao;
  String codigoAnvisa;
  String unidade;
  String classeTerapeutica;
  LocalDate criadoEm;
}
```

### Lote
```java
@Entity
class Lote {
  @Id UUID id;
  @ManyToOne Produto produto;
  String numeroLote;
  LocalDate dataFabricacao;
  LocalDate dataValidade;
  BigDecimal quantidade;
  @Enumerated(EnumType.STRING) LoteStatus status;
}
```

### OrdemProducao
```java
@Entity
class OrdemProducao {
  @Id UUID id;
  String numeroOP;
  @ManyToOne Produto produto;
  BigDecimal quantidadePlanejada;
  LocalDate dataInicio;
  LocalDate dataFimPrevista;
  @Enumerated(EnumType.STRING) OPStatus status;
}
```

### InventarioItem
```java
@Entity
class InventarioItem {
  @Id UUID id;
  @ManyToOne Produto produto;
  @ManyToOne Lote lote;
  BigDecimal quantidade;
  String localizacao;
}
```

### MovimentoEstoque
```java
@Entity
class MovimentoEstoque {
  @Id UUID id;
  @Enumerated(EnumType.STRING) MovimentoTipo tipo;
  @ManyToOne Produto produto;
  @ManyToOne Lote lote;
  BigDecimal quantidade;
  LocalDateTime dataHora;
  @ManyToOne Usuario responsavel;
}
```

### TesteQualidade
```java
@Entity
class TesteQualidade {
  @Id UUID id;
  @ManyToOne Lote lote;
  String tipoTeste;
  @Enumerated(EnumType.STRING) ResultadoTeste resultado;
  LocalDateTime dataTeste;
  @ManyToOne Usuario responsavel;
}
```

### RegistroRastreabilidade
```java
@Entity
class RegistroRastreabilidade {
  @Id UUID id;
  @ManyToOne Lote lote;
  String evento;
  String descricao;
  LocalDateTime dataHora;
  @ManyToOne Usuario usuario;
}
```

### Usuário e Perfis
```java
@Entity
class Usuario {
  @Id UUID id;
  String nome;
  String email;
  String senhaHash;
  @ManyToMany List<Role> roles;
}

@Entity
class Role {
  @Id UUID id;
  String nome;
}
```

---

## ⚙️ Regras de Negócio

- Não liberar lote sem todos os testes de qualidade “APROVADO”.
- Auditoria obrigatória em toda modificação de entidades críticas.
- Cada Ordem de Produção gera um Lote.
- Movimentos de estoque refletem no InventarioItem (entrada/saída/ajuste).
- Toda ação cria um registro em RegistroRastreabilidade.
- Usuários autenticados via JWT (RBAC).

---

## 📡 API REST (Spring Controller — endpoints resumidos)

### /api/produtos
- GET / → listar
- POST / → criar
- GET /{id} → buscar
- PUT /{id} → atualizar
- DELETE /{id} → remover

### /api/lotes
- POST / → criar (a partir de OP)
- GET /{id} → detalhes
- PUT /{id}/status → alterar status (liberação, bloqueio)
- GET /rastrear?numero=XXX → retorna histórico do lote

### /api/ordens-producao
- POST / → emitir OP
- GET /ativas → listar OPs em andamento
- PUT /{id}/finalizar

### /api/qualidade
- POST /teste → registrar teste
- PUT /teste/{id}/resultado → atualizar resultado
- GET /lote/{loteId} → listar testes por lote

### /api/estoque
- GET / → inventário atual
- POST /movimento → registrar entrada/saída/ajuste

### /api/usuarios
- POST /login → autenticação JWT
- GET /me → informações do usuário logado
- POST / → criar usuário (admin)

---

## 🧩 Enums
```java
enum LoteStatus { EM_PRODUCAO, LIBERADO, BLOQUEADO, CANCELADO }
enum ResultadoTeste { APROVADO, REPROVADO, PENDENTE }
enum MovimentoTipo { ENTRADA, SAIDA, AJUSTE }
enum OPStatus { PLANEJADA, EM_EXECUCAO, CONCLUIDA }
```

---

## 🧪 Testes Automatizados
- JUnit + Testcontainers para integração com PostgreSQL.
- MockMvc para testes REST.
- Cobertura mínima: 80%.
- Flyway valida schema no startup.
- Auditoria testada para alterações.

---

## 🧰 Docker Compose
```yaml
version: "3.8"
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: beepharma
      POSTGRES_USER: bee
      POSTGRES_PASSWORD: pharma
    ports: ["5432:5432"]
  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/beepharma
      SPRING_DATASOURCE_USERNAME: bee
      SPRING_DATASOURCE_PASSWORD: pharma
    ports: ["8080:8080"]
    depends_on: [postgres]
```

---

## 🔒 Segurança e conformidade
- JWT expira em 1h.
- Logs de auditoria imutáveis.
- Backup diário do banco.
- Retenção mínima: 5 anos.
- Usuário Admin criado no startup.

---

## 🚀 Etapas de desenvolvimento

1. Criar projeto base Spring Boot.
2. Implementar entidades JPA, repositório.
3. Criar services com regras de negócio e logs de auditoria.
4. Implementar controllers REST.
7. Subir via Docker Compose.
8. Documentar com Swagger (Springdoc).
9. Validar fluxo principal: Produto → OP → Lote → Teste QC → Liberação → Movimento → Rastreabilidade.

### UML - Diagrama de Classe
@startuml
' BeePharma - Diagrama de Classes (resumo)
' Entidades principais, atributos e associações

class Produto {
  +id: UUID
  +nome: String
  +descricao: String
  +codigoANVISA: String
  +unidade: String
  +classe_terapeutica: String
  +criar(): Produto
}

class Lote {
  +id: UUID
  +numeroLote: String
  +dataFabricacao: Date
  +dataValidade: Date
  +quantidade: Decimal
  +status: LoteStatus
  +registrarMovimento(qty, tipo): void
}

class OrdemProducao {
  +id: UUID
  +numeroOP: String
  +produtoId: UUID
  +quantidadePlanejada: Decimal
  +dataInicio: Date
  +dataFimPrevista: Date
  +status: OPStatus
  +gerarLote(): Lote
}

class InventarioItem {
  +id: UUID
  +produtoId: UUID
  +loteId: UUID
  +quantidade: Decimal
  +localizacao: String
}

class MovimentoEstoque {
  +id: UUID
  +tipo: MovimentoTipo
  +produtoId: UUID
  +loteId: UUID
  +quantidade: Decimal
  +dataHora: DateTime
  +responsavelId: UUID
}

class Fornecedor {
  +id: UUID
  +nome: String
  +cnpj: String
  +contato: String
}

class PedidoCompra {
  +id: UUID
  +fornecedorId: UUID
  +itens: List<PedidoItem>
  +status: PedidoStatus
  +dataPedido: Date
}

class PedidoItem {
  +produtoId: UUID
  +quantidade: Decimal
  +precoUnitario: Decimal
}

class TesteQualidade {
  +id: UUID
  +loteId: UUID
  +tipoTeste: String
  +resultado: ResultadoTeste
  +dataTeste: DateTime
  +responsavelId: UUID
}

class RegistroRastreabilidade {
  +id: UUID
  +loteId: UUID
  +evento: String
  +descricao: String
  +dataHora: DateTime
  +usuarioId: UUID
}

class Usuario {
  +id: UUID
  +nome: String
  +email: String
  +senhaHash: String
  +roles: List<Role>
  +autenticar(): boolean
}

class Role {
  +id: UUID
  +nome: String
  +permissoes: List<String>
}

class Auditoria {
  +id: UUID
  +entidade: String
  +entidadeId: UUID
  +acao: String
  +dataHora: DateTime
  +usuarioId: UUID
}

' Relacionamentos
Produto "1" -- "0..*" Lote : produz
OrdemProducao "1" o-- "0..*" Lote : gera
Produto "1" -- "0..*" InventarioItem : armazenado_em
InventarioItem "1" -- "0..*" MovimentoEstoque : referencia
Fornecedor "1" -- "0..*" PedidoCompra : recebe
PedidoCompra "1" -- "0..*" PedidoItem : contem
Lote "1" -- "0..*" TesteQualidade : submetido_a
Lote "1" -- "0..*" RegistroRastreabilidade : tem
Usuario "1" -- "0..*" Auditoria : gera

' Enumerations (simples)
enum LoteStatus {
  EM_PRODUCAO
  LIBERADO
  BLOQUEADO
  CANCELADO
}

enum ResultadoTeste {
  APROVADO
  REPROVADO
  PENDENTE
}

enum MovimentoTipo {
  ENTRADA
  SAIDA
  AJUSTE
  TRANSFERENCIA
}

@enduml

### UML Diagrama de caso de uso

@startuml
left to right direction
actor "Operador de Produção" as Op
actor "Supervisor de Produção" as Sup
actor "Inspetor de Qualidade" as IQ
actor "Gerente de Logística" as GL
actor "Fornecedor" as Forn
actor "Administrador" as Admin

rectangle "BeePharma - Sistema Integrado" {
  (Emitir Ordem de Produção) as OP
  (Registrar Produção / Criar Lote) as RegLote
  (Registrar Teste de Qualidade) as RegTeste
  (Aprovar/Reprovar Lote) as AprovaLote
  (Gerenciar Estoque) as Estoque
  (Registrar Entrada de Materiais) as EntMat
  (Gerar Relatórios / Auditoria) as Rel
  (Rastrear Produto / Lote) as Rastreio
  (Gerenciar Fornecedores) as GerFor
  (Gerenciar Usuários e Perfis) as GerUS
  (Integrar com ERP/SCADA) as Integracao
}

Op --> OP
Op --> RegLote
Sup --> AprovaLote
IQ --> RegTeste
IQ --> AprovaLote
GL --> Estoque
GL --> Rastreio
Forn --> EntMat
Admin --> GerUS
Admin --> GerFor
Admin --> Rel
Integracao <..> OP
Rastreio ..> Rel

@enduml

