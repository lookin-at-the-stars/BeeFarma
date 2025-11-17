# 🐝 BeePharma - Comandos Úteis

## 🚀 Quick Commands

### Desenvolvimento Local

```bash
# Frontend Angular
cd frontend-angular
npm install
npm start                    # http://localhost:4200
npm run build               # Build produção
npm run lint                # Verificar código

# Backend Spring Boot
./mvnw spring-boot:run      # http://localhost:8080
./mvnw clean package        # Build JAR
./mvnw test                 # Testes
```

### Docker

```bash
# Subir com frontend Angular
docker-compose -f compose-angular.yaml up --build
docker-compose -f compose-angular.yaml up -d          # Background
docker-compose -f compose-angular.yaml down           # Parar

# Apenas frontend
docker-compose -f compose-angular.yaml up --build frontend-angular

# Apenas backend
docker-compose -f compose-angular.yaml up --build backend

# Ver logs
docker-compose -f compose-angular.yaml logs -f
docker-compose -f compose-angular.yaml logs -f frontend-angular
docker-compose -f compose-angular.yaml logs -f backend

# Rebuild específico
docker-compose -f compose-angular.yaml build --no-cache frontend-angular
```

### Angular CLI

```bash
cd frontend-angular

# Gerar componente
npx @angular/cli generate component components/meu-componente
ng g c components/meu-componente                      # Atalho

# Gerar serviço
npx @angular/cli generate service services/meu-servico
ng g s services/meu-servico                            # Atalho

# Gerar model
mkdir -p src/app/models
touch src/app/models/meu-model.model.ts

# Servidor de desenvolvimento
ng serve                                               # Porta 4200
ng serve --port 3000                                  # Porta customizada
ng serve --open                                       # Abre browser

# Build
ng build                                              # Produção
ng build --configuration development                  # Desenvolvimento
```

### Git

```bash
# Commitar mudanças
git add .
git commit -m "feat: implementa frontend Angular"
git push origin main

# Criar branch para feature
git checkout -b feature/novos-componentes
git push -u origin feature/novos-componentes

# Ver mudanças
git status
git diff
git log --oneline
```

### Limpeza

```bash
# Limpar node_modules e reinstalar
cd frontend-angular
rm -rf node_modules package-lock.json
npm install

# Limpar build
rm -rf dist/

# Limpar cache do Docker
docker system prune -a
docker volume prune

# Limpar tudo
docker-compose -f compose-angular.yaml down -v
rm -rf frontend-angular/node_modules
rm -rf frontend-angular/dist
```

### Debugging

```bash
# Ver versões
node --version
npm --version
ng version

# Verificar portas em uso
lsof -i :80
lsof -i :4200
lsof -i :8080
lsof -i :5432

# Matar processo em porta
kill -9 $(lsof -t -i:4200)

# Ver logs do container
docker logs beepharma-frontend-angular
docker logs beepharma-backend
docker logs beepharma-postgres

# Entrar no container
docker exec -it beepharma-frontend-angular sh
docker exec -it beepharma-backend bash
docker exec -it beepharma-postgres psql -U beepharma
```

### Testes

```bash
# Angular
cd frontend-angular
ng test                     # Testes unitários
ng e2e                      # Testes e2e (se configurado)

# Backend
./mvnw test                 # Todos os testes
./mvnw test -Dtest=ProdutoServiceTest    # Teste específico
```

### Performance

```bash
# Análise do bundle Angular
cd frontend-angular
npm run build -- --stats-json
npx webpack-bundle-analyzer dist/frontend-angular/stats.json

# Profile build
ng build --profile
```

### Banco de Dados

```bash
# Conectar ao PostgreSQL
docker exec -it beepharma-postgres psql -U beepharma

# Dentro do psql:
\dt                         # Listar tabelas
\d produtos                 # Descrever tabela
SELECT * FROM produtos;     # Query
\q                          # Sair

# Backup
docker exec beepharma-postgres pg_dump -U beepharma beepharma > backup.sql

# Restore
docker exec -i beepharma-postgres psql -U beepharma beepharma < backup.sql
```

### Produção

```bash
# Build otimizado
cd frontend-angular
ng build --configuration production

# Copiar para servidor
scp -r dist/frontend-angular/* user@server:/var/www/beepharma/

# Nginx reload (no servidor)
sudo nginx -t
sudo systemctl reload nginx
```

## 📝 Atalhos VSCode

```
Ctrl+P          # Quick open
Ctrl+Shift+P    # Command palette
Ctrl+`          # Toggle terminal
Ctrl+B          # Toggle sidebar
F5              # Debug
```

## 🔧 Configurações Úteis

### package.json scripts

```json
{
  "scripts": {
    "start": "ng serve",
    "build": "ng build",
    "build:prod": "ng build --configuration production",
    "watch": "ng build --watch",
    "test": "ng test",
    "lint": "ng lint",
    "format": "prettier --write \"src/**/*.{ts,html,scss}\""
  }
}
```

### .gitignore adições

```
# Angular
/frontend-angular/node_modules/
/frontend-angular/dist/
/frontend-angular/.angular/
/frontend-angular/npm-debug.log

# IDE
.idea/
.vscode/
*.swp
*.swo
```

## 🐛 Troubleshooting

### Problema: Port already in use

```bash
# Linux/Mac
lsof -ti:4200 | xargs kill -9
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :4200
taskkill /PID <PID> /F
```

### Problema: npm install falha

```bash
# Limpar cache
npm cache clean --force
rm -rf node_modules package-lock.json
npm install

# Usar npm ci (mais rápido e determinístico)
npm ci
```

### Problema: Docker build falha

```bash
# Rebuild sem cache
docker-compose -f compose-angular.yaml build --no-cache

# Ver logs detalhados
docker-compose -f compose-angular.yaml up --build --no-start
docker-compose -f compose-angular.yaml logs
```

### Problema: CORS error

```java
// Adicionar no backend
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost"})
```

## 📚 Recursos

- Angular Docs: https://angular.dev
- TypeScript Docs: https://www.typescriptlang.org
- RxJS Docs: https://rxjs.dev
- Docker Docs: https://docs.docker.com
- Spring Boot Docs: https://spring.io/projects/spring-boot

---

**BeePharma** 🐝
