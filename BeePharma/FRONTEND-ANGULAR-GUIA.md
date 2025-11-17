# 🐝 BeePharma - Novo Frontend Angular

## 🎉 O que foi criado?

Transformei o frontend simples HTML/CSS/JS em uma **aplicação Angular moderna e profissional** com:

### ✨ Tecnologias

- **Angular 20** (versão mais recente)
- **TypeScript** para tipagem forte
- **SCSS** para estilos avançados
- **Signals** para gerenciamento de estado reativo
- **RxJS** para programação reativa
- **Arquitetura modular** e escalável

### 🎨 Design Melhorado

- **Tema Bee** com gradientes amarelo/dourado (#FFB300, #FFA000)
- **Cards modernos** com sombras e efeitos hover
- **Animações suaves** (fade-in, slide-up, float)
- **Responsivo** - funciona perfeitamente em mobile
- **Tabelas elegantes** com hover effects
- **Modais profissionais** com backdrop
- **Botões com gradientes** e feedback visual

### 📂 Estrutura Criada

```
frontend-angular/
├── src/app/
│   ├── components/
│   │   ├── dashboard/      # ✅ Dashboard com estatísticas
│   │   ├── produtos/       # ✅ CRUD completo de produtos
│   │   ├── lotes/          # 🚧 Template básico
│   │   ├── estoque/        # 🚧 Template básico
│   │   ├── inventario/     # 🚧 Template básico
│   │   └── producao/       # 🚧 Template básico
│   ├── services/           # Serviços de comunicação com API
│   │   ├── produto.ts
│   │   ├── lote.ts
│   │   ├── movimento-estoque.ts
│   │   ├── inventario.ts
│   │   └── ordem-producao.ts
│   ├── models/             # Interfaces TypeScript
│   │   ├── produto.model.ts
│   │   ├── lote.model.ts
│   │   ├── movimento-estoque.model.ts
│   │   ├── inventario.model.ts
│   │   └── ordem-producao.model.ts
│   ├── app.routes.ts       # Rotas da aplicação
│   ├── app.config.ts       # Configuração global
│   └── app.scss            # Estilos do header/layout
└── src/styles.scss         # Estilos globais
```

## 🚀 Como Executar

### Opção 1: Desenvolvimento Local

```bash
cd BeePharma/frontend-angular
npm install
npm start
```

Acesse: `http://localhost:4200`

### Opção 2: Com Docker (Recomendado)

```bash
# Subir toda a aplicação (backend + postgres + frontend Angular)
docker-compose -f compose-angular.yaml up --build

# Ou apenas rebuild do frontend
docker-compose -f compose-angular.yaml up --build frontend-angular
```

Acesse: `http://localhost`

## 🎯 Funcionalidades Implementadas

### ✅ Dashboard
- Exibe estatísticas em tempo real:
  - Total de produtos cadastrados
  - Total de lotes ativos
  - Total de movimentações
  - Total de ordens de produção
- Cards com ícones e gradientes
- Seção de boas-vindas com features
- Loading spinner durante carregamento

### ✅ Produtos (CRUD Completo)
- **Listar** todos os produtos em tabela elegante
- **Criar** novo produto com modal
- **Editar** produto existente
- **Excluir** produto com confirmação
- **Validação** de campos obrigatórios
- **Formatação** de datas
- **Empty state** quando não há produtos

Campos do produto:
- Nome *
- Código ANVISA
- Princípio Ativo *
- Classe Terapêutica
- Unidade *
- Descrição

### 🚧 Outros Módulos
Os componentes Lotes, Estoque, Inventário e Produção foram criados com templates básicos e podem ser expandidos seguindo o mesmo padrão do componente de Produtos.

## 🎨 Destaques Visuais

### Header Fixo
- Gradiente amarelo/dourado
- Logo com abelha animada 🐝
- Navegação com links ativos destacados
- Responsivo para mobile

### Cards
- Sombras suaves
- Efeito hover com elevação
- Gradientes nos ícones
- Números grandes e coloridos

### Tabelas
- Header com gradiente
- Linhas alternadas com hover
- Responsivas com scroll horizontal em mobile
- Badges coloridos por status

### Modais
- Backdrop escuro semitransparente
- Animação slide-up
- Header com gradiente
- Botão X com rotação no hover
- Formulários organizados em grid

### Botões
- Gradientes
- Hover com elevação
- Feedback visual no clique
- Variantes: primary, secondary, success, danger
- Tamanhos: sm, md, lg

## 📡 Integração com Backend

Todos os serviços estão configurados para se comunicar com a API:

```typescript
// Exemplo de uso no componente
private produtoService = inject(ProdutoService);

this.produtoService.getAll().subscribe({
  next: (produtos) => console.log(produtos),
  error: (error) => console.error(error)
});
```

### Endpoints Configurados
- `/api/produtos` - CRUD de produtos
- `/api/lotes` - CRUD de lotes
- `/api/movimentos-estoque` - Movimentações
- `/api/inventario` - Inventário
- `/api/ordens-producao` - Ordens de produção

## 🔧 Configurações

### Proxy API (desenvolvimento)
Durante desenvolvimento, as requisições `/api/*` são automaticamente redirecionadas para `http://localhost:8080`

### Nginx (produção)
O arquivo `nginx.conf` já está configurado para fazer proxy das requisições `/api/*` para o backend.

## 📝 Próximos Passos Sugeridos

1. **Implementar outros CRUDs** seguindo o padrão de Produtos
2. **Adicionar toasts** para notificações melhores
3. **Implementar filtros e busca** nas tabelas
4. **Adicionar paginação** para grandes volumes de dados
5. **Criar gráficos** com bibliotecas como Chart.js ou NgxCharts
6. **Implementar autenticação** com JWT
7. **Adicionar testes** unitários e e2e
8. **Criar documentação** de componentes com Storybook

## 🐛 Troubleshooting

### Build falha
```bash
cd frontend-angular
rm -rf node_modules package-lock.json
npm install
npm run build
```

### Backend não responde
Verifique se o backend está rodando na porta 8080 e se o CORS está configurado corretamente.

### Styles não aplicam
Limpe o cache do navegador ou execute em modo anônimo.

## 💡 Dicas de Desenvolvimento

### Hot Reload
O Angular CLI tem hot reload automático. Qualquer mudança nos arquivos atualiza automaticamente o navegador.

### Debugging
Use o Angular DevTools (extensão do Chrome) para debugar componentes e performance.

### Linting
```bash
npm run lint
```

## 📚 Documentação Útil

- [Angular Docs](https://angular.dev)
- [Angular Material](https://material.angular.io) - para componentes UI adicionais
- [RxJS](https://rxjs.dev) - para operadores reativos
- [TypeScript](https://www.typescriptlang.org) - documentação do TypeScript

---

## 🎊 Resultado Final

O frontend foi completamente transformado de uma aplicação vanilla JS para uma **aplicação Angular moderna, escalável e profissional**, mantendo a identidade visual do tema "abelha" mas com um design muito mais polido e moderno.

**Principais melhorias:**
- ✅ Arquitetura modular e organizada
- ✅ Tipagem forte com TypeScript
- ✅ Gerenciamento de estado com Signals
- ✅ Comunicação reativa com a API
- ✅ Design responsivo e moderno
- ✅ Animações suaves
- ✅ Código reutilizável e manutenível
- ✅ Pronto para crescer e escalar

**BeePharma** 🐝 agora tem um frontend de nível profissional!
