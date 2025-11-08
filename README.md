# BeePharma - Sistema de Gestão Farmacêutica 🐝

## 📋 Visão Geral
BeePharma é um sistema completo de gestão para indústrias farmacêuticas, focado no controle de estoque, lotes e produção de medicamentos. O sistema foi desenvolvido pensando na segurança e rastreabilidade que o setor farmacêutico exige.

## � Como Executar

### Pré-requisitos
- Docker
- Docker Compose

### Iniciar o Sistema Completo

```bash
# No diretório do projeto
docker compose up --build
```

Isso irá iniciar 3 serviços:
- **MariaDB** na porta 3306
- **Backend (Spring Boot)** na porta 8080
- **Frontend (Nginx)** na porta 80

### Acessar o Sistema

- **🌐 Frontend**: http://localhost
- **🔧 API Backend**: http://localhost:8080/api
- **📚 Swagger UI**: http://localhost:8080/api/swagger-ui.html

## 🛠 Tecnologias Utilizadas

### Backend
- **Java 17**: A linguagem de programação principal, escolhida por sua robustez e forte tipagem
- **Spring Boot 3.1.5**: Framework que facilita a criação de aplicações Java, oferecendo diversos recursos prontos para uso
  - Imagine o Spring Boot como um "kit de ferramentas" que já vem com tudo pronto para construir uma casa

### Frontend
- **HTML5/CSS3/JavaScript**: Interface moderna e responsiva
- **Nginx**: Servidor web para servir a aplicação frontend
- **Tema Abelha**: Design em amarelo/dourado suave inspirado em abelhas 🐝

### Banco de Dados
- **MariaDB**: Banco de dados relacional (parecido com MySQL) onde guardamos todas as informações
  - Pense no banco de dados como um grande arquivo de Excel, mas muito mais organizado e seguro

### Ferramentas de Desenvolvimento
- **Maven**: Gerenciador de dependências e construção do projeto
  - Como se fosse um "lista de compras" do projeto, que busca tudo que precisamos automaticamente
- **Docker**: Ferramenta para criar "contêineres" que empacotam nossa aplicação
  - Imagine containers como "caixas mágicas" que guardam todo o ambiente necessário para rodar o sistema
- **Flyway**: Gerenciador de versões do banco de dados
  - Como um "histórico de mudanças" do banco de dados, garantindo que todas as alterações são aplicadas corretamente

### Bibliotecas Importantes
- **Lombok**: Reduz a quantidade de código que precisamos escrever
- **MapStruct**: Ajuda a converter dados entre diferentes partes do sistema
- **Hibernate**: Ferramenta que conecta nosso código Java com o banco de dados

## 📁 Estrutura de Diretórios

### `src/main/java/br/com/beepharma/`
A casa do nosso código fonte, organizada em várias "gavetas":

#### 📂 `application/`
- **dto/**: Objetos que carregam dados entre diferentes partes do sistema
  - Como "formulários" que usamos para transportar informações
- **service/**: Contém a lógica de negócio do sistema
  - O "cérebro" da aplicação, onde as regras do negócio são implementadas

#### 📂 `domain/`
- **entity/**: As principais "coisas" que nosso sistema gerencia
  - Como "fichas" de produtos, lotes, etc.
- **enums/**: Lista de opções pré-definidas
  - Como uma "lista de escolhas" fixa
- **repository/**: Responsável por guardar e buscar dados no banco
  - Como "bibliotecários" que organizam e buscam informações

#### 📂 `infrastructure/`
- **config/**: Configurações do sistema
  - As "preferências" e "ajustes" do sistema

#### 📂 `presentation/`
- **controller/**: Recebe as requisições dos usuários
  - Como "atendentes" que recebem e direcionam pedidos
- **exception/**: Trata erros e problemas
  - Como "planos de contingência" para quando algo dá errado
- **handler/**: Processa erros de forma amigável
  - Transforma erros técnicos em mensagens que fazem sentido

#### 📂 `util/`
- Ferramentas úteis usadas em todo o sistema
  - Como uma "caixa de ferramentas" com itens que usamos frequentemente

### `src/main/resources/`
- Arquivos de configuração e recursos
  - Como um "manual de instruções" do sistema
- **db/migration/**: Scripts de banco de dados
  - "Receitas" para criar e modificar o banco de dados

## 🏗 Arquitetura do Sistema

O BeePharma segue uma arquitetura em camadas, como um prédio:

1. **Camada de Apresentação** (Presentation Layer)
   - A "fachada" do sistema
   - Recebe requisições e retorna respostas
   - Encontrada em `presentation/`

2. **Camada de Aplicação** (Application Layer)
   - O "escritório" do sistema
   - Coordena as operações e aplica regras de negócio
   - Encontrada em `application/`

3. **Camada de Domínio** (Domain Layer)
   - O "coração" do sistema
   - Contém as regras mais importantes do negócio
   - Encontrada em `domain/`

4. **Camada de Infraestrutura** (Infrastructure Layer)
   - O "alicerce" do sistema
   - Lida com banco de dados e recursos externos
   - Encontrada em `infrastructure/`

## 🔄 Fluxo de Funcionamento

1. Uma requisição chega ao sistema (ex: cadastrar um produto)
2. O Controller (presentation) recebe essa requisição
3. Passa para o Service (application) processar
4. O Service usa Repositories (domain) para salvar/buscar dados
5. Os dados são salvos no banco através da Infrastructure
6. A resposta volta pelo mesmo caminho

É como uma carta passando por vários departamentos dos correios até chegar ao destinatário!

## 🎯 Padrões de Projeto Utilizados

- **DTO (Data Transfer Object)**
  - Como "formulários" específicos para cada operação
- **Repository**
  - Como "arquivistas" especializados em dados
- **Service**
  - Como "gerentes" que coordenam as operações
- **Controller**
  - Como "recepcionistas" que direcionam as requisições

## 🔒 Segurança e Boas Práticas

- Senhas são armazenadas de forma segura (criptografadas)
- Todas as alterações no banco são rastreadas (Flyway)
- Código segue padrões de qualidade
- Transações garantem que operações são feitas por completo ou não são feitas

## 🚀 Como o Sistema Cresce

O sistema foi projetado para crescer de forma organizada:
- Cada parte tem sua responsabilidade bem definida
- Novos recursos podem ser adicionados sem mexer nos existentes
- Mudanças no banco são controladas e versionadas
- Testes garantem que tudo continua funcionando

Esta estrutura permite que o sistema cresça de forma saudável, como uma colmeia bem organizada! 🐝