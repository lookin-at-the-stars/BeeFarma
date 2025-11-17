# 🐝 BeePharma - Frontend Angular

## ✨ Transformação Completa!

O frontend do BeePharma foi **completamente recriado** usando **Angular 20**, transformando de uma aplicação vanilla JS simples em uma **aplicação moderna, profissional e escalável**.

---

## 🎨 O Que Mudou?

### Antes (HTML/CSS/JS)
- ❌ Código JavaScript simples sem estrutura
- ❌ Difícil manutenção e escalabilidade
- ❌ Sem tipagem
- ❌ Estado global desorganizado

### Agora (Angular 20)
- ✅ **Framework moderno** e robusto
- ✅ **TypeScript** com tipagem forte
- ✅ **Arquitetura modular** (componentes + serviços + models)
- ✅ **Signals** para estado reativo
- ✅ **RxJS** para programação assíncrona
- ✅ **Roteamento** profissional
- ✅ **Design System** consistente
- ✅ **Animações** suaves e modernas
- ✅ **Totalmente responsivo**

---

## 🚀 Quick Start

### Opção 1: Docker (Mais Fácil)

```bash
# Clone e entre no diretório
cd BeePharma

# Execute o script de teste
./test-angular-frontend.sh

# Ou manualmente:
docker-compose -f compose-angular.yaml up --build
```

**Acesse:** http://localhost

### Opção 2: Desenvolvimento Local

```bash
# Backend (Terminal 1)
cd BeePharma
./mvnw spring-boot:run

# Frontend (Terminal 2)
cd BeePharma/frontend-angular
npm install
npm start
```

**Acesse:** http://localhost:4200

---

## 📂 Estrutura do Projeto

```
frontend-angular/
├── src/
│   ├── app/
│   │   ├── components/           # Componentes visuais
│   │   │   ├── dashboard/       # ✅ Implementado
│   │   │   ├── produtos/        # ✅ CRUD completo
│   │   │   ├── lotes/           # 🚧 Em desenvolvimento
│   │   │   ├── estoque/         # 🚧 Em desenvolvimento
│   │   │   ├── inventario/      # 🚧 Em desenvolvimento
│   │   │   └── producao/        # 🚧 Em desenvolvimento
│   │   ├── services/            # Comunicação com API
│   │   │   ├── produto.ts
│   │   │   ├── lote.ts
│   │   │   ├── movimento-estoque.ts
│   │   │   ├── inventario.ts
│   │   │   └── ordem-producao.ts
│   │   ├── models/              # Interfaces TypeScript
│   │   │   ├── produto.model.ts
│   │   │   ├── lote.model.ts
│   │   │   └── ...
│   │   ├── app.routes.ts        # Configuração de rotas
│   │   ├── app.config.ts        # Config da aplicação
│   │   ├── app.ts               # Componente raiz
│   │   ├── app.html             # Template principal
│   │   └── app.scss             # Estilos do header
│   ├── styles.scss              # Estilos globais
│   └── index.html               # HTML principal
├── angular.json                 # Configuração Angular CLI
├── package.json                 # Dependências
└── tsconfig.json                # Config TypeScript
```

---

## 🎯 Funcionalidades

### ✅ Implementadas

#### Dashboard
- Cards com estatísticas em tempo real
- Animações de entrada suaves
- Ícones com gradientes
- Loading states
- Seção de boas-vindas

#### Produtos (CRUD Completo)
- **Listar** produtos em tabela elegante
- **Criar** novo produto via modal
- **Editar** produto existente
- **Excluir** com confirmação
- Validação de formulários
- Empty states
- Loading states
- Formatação de datas

### 🚧 Em Desenvolvimento

Componentes criados com estrutura básica, prontos para implementação:
- Lotes
- Estoque
- Inventário
- Produção

---

## 🎨 Design System

### Cores (Tema Bee)
```scss
--primary-color: #FFB300     // Amarelo dourado
--primary-dark: #FF8F00      // Laranja escuro
--primary-light: #FFD54F     // Amarelo claro
--accent-color: #FFA000      // Laranja
```

### Componentes
- **Botões:** Com gradientes e hover effects
- **Cards:** Sombras suaves e animações
- **Tabelas:** Headers com gradiente, hover em linhas
- **Modais:** Backdrop, animação slide-up
- **Forms:** Validação visual, focus states
- **Badges:** Coloridos por status
- **Spinners:** Loading animado

### Animações
- Fade In (entrada de páginas)
- Slide Up (modais)
- Float (ícone da abelha)
- Hover effects (cards, botões, links)

---

## 🔌 Integração com Backend

### Serviços TypeScript

Cada módulo tem seu serviço:

```typescript
// Exemplo: ProdutoService
getAll(): Observable<Produto[]>
getById(id: string): Observable<Produto>
create(produto: Produto): Observable<Produto>
update(id: string, produto: Produto): Observable<Produto>
delete(id: string): Observable<void>
```

### Configuração de API

- **Dev:** Proxy automático `/api/*` → `http://localhost:8080`
- **Prod:** Nginx faz proxy `/api/*` → `http://backend:8080`

---

## 📱 Responsividade

O design é **mobile-first** e adapta-se perfeitamente:

- **Desktop:** Layout completo com sidebar
- **Tablet:** Grid adaptativo
- **Mobile:** Menu colapsável, cards empilhados

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Angular | 20.x | Framework principal |
| TypeScript | 5.x | Linguagem |
| SCSS | - | Estilos avançados |
| RxJS | 7.x | Programação reativa |
| Signals | Angular 20 | Estado reativo |
| HttpClient | Angular | Requisições HTTP |

---

## 📚 Documentação

- **[FRONTEND-ANGULAR-GUIA.md](./FRONTEND-ANGULAR-GUIA.md)** - Guia completo
- **[FRONTEND-ANGULAR-README.md](./FRONTEND-ANGULAR-README.md)** - README técnico

---

## 🐛 Troubleshooting

### Build falha
```bash
cd frontend-angular
rm -rf node_modules package-lock.json
npm install
npm run build
```

### Portas em uso
```bash
# Parar containers
docker-compose -f compose-angular.yaml down

# Verificar portas
lsof -i :80
lsof -i :8080
```

### CORS errors
Verifique se o backend está configurado para aceitar requisições de `http://localhost:4200`

---

## 🎯 Próximos Passos

1. ✅ ~~Criar estrutura Angular~~
2. ✅ ~~Implementar Dashboard~~
3. ✅ ~~Implementar CRUD de Produtos~~
4. 🚧 Implementar CRUD de Lotes
5. 🚧 Implementar gestão de Estoque
6. 🚧 Implementar Inventário
7. 🚧 Implementar Ordens de Produção
8. 📝 Adicionar testes unitários
9. 📝 Adicionar toasts/notificações
10. 📝 Implementar autenticação JWT

---

## 📸 Screenshots

### Dashboard
![Dashboard com cards de estatísticas e seção de boas-vindas]

### Produtos
![Tabela de produtos com botões de ação]

### Modal de Produto
![Formulário de cadastro/edição de produto]

---

## 🤝 Contribuindo

Para adicionar novos componentes, siga o padrão do componente **Produtos**:

1. Criar serviço na pasta `services/`
2. Criar model na pasta `models/`
3. Implementar componente com `.ts`, `.html` e `.scss`
4. Adicionar rota em `app.routes.ts`
5. Adicionar link no header em `app.html`

---

## 📄 Licença

Proprietário - BeePharma

---

## 🎉 Conclusão

O frontend do **BeePharma** agora é uma **aplicação Angular moderna e profissional**, pronta para escalar e receber novos recursos. O design mantém a identidade visual do tema "abelha" mas com um visual muito mais polido e moderno.

**Principais conquistas:**
- ✅ Framework moderno (Angular 20)
- ✅ Código organizado e manutenível
- ✅ Tipagem forte com TypeScript
- ✅ Design responsivo e elegante
- ✅ Arquitetura escalável
- ✅ Pronto para produção

---

**BeePharma** 🐝 - Sistema de Gestão Farmacêutica

*Desenvolvido com Angular 20 + TypeScript + SCSS*
