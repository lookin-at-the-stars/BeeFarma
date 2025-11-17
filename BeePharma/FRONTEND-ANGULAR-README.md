# BeePharma Frontend - Angular

Frontend moderno desenvolvido em Angular 20 para o sistema BeePharma.

## 🚀 Tecnologias

- **Angular 20** - Framework principal  
- **TypeScript** - Linguagem de programação
- **SCSS** - Pré-processador CSS
- **RxJS** - Programação reativa
- **Signals** - Gerenciamento de estado moderno
- **HttpClient** - Comunicação com API

## 📁 Estrutura

```
frontend-angular/
├── src/app/
│   ├── components/     # Componentes (Dashboard, Produtos, etc.)
│   ├── services/       # Serviços de API
│   ├── models/         # Interfaces TypeScript
│   └── app.routes.ts   # Configuração de rotas
```

## 🎨 Design

- Tema inspirado em abelhas (amarelo/dourado)
- Design moderno e responsivo
- Animações suaves
- Cards com gradientes

## 🛠️ Desenvolvimento

```bash
cd frontend-angular
npm install
npm start  # http://localhost:4200
npm run build  # Build para produção
```

## 🐳 Docker

```bash
docker build -f Dockerfile.frontend-angular -t beepharma-frontend .
docker run -p 80:80 beepharma-frontend
```

## ✅ Funcionalidades

- ✅ Dashboard com estatísticas
- ✅ Gestão de Produtos (CRUD completo)
- 🚧 Lotes, Estoque, Inventário, Produção (em desenvolvimento)

---
**BeePharma** 🐝
