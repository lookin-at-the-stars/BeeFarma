# 🐝 Deploy BeePharma na AWS

## 📋 Pré-requisitos
- [x] Conta AWS com $100 crédito
- [ ] AWS CLI instalado
- [ ] Docker Desktop rodando
- [ ] Domínio (opcional, mas recomendado)

---

## 🎯 Arquitetura Proposta

```
Internet → ALB (Load Balancer) → ECS Fargate (2 containers)
                                 ├─ Backend (Spring Boot)
                                 └─ Frontend (Nginx)
                                      ↓
                                 RDS MariaDB
```

**Custo Estimado**: ~$30-40/mês (ou $0 com créditos por 2-3 meses)

---

## 🚀 Opção 1: Deploy Rápido com ECS Fargate (RECOMENDADO)

### Passo 1: Instalar AWS CLI

```powershell
# Baixe e instale AWS CLI
winget install Amazon.AWSCLI

# Configure suas credenciais
aws configure
# AWS Access Key ID: [cole sua key]
# AWS Secret Access Key: [cole sua secret]
# Default region: us-east-1
# Default output: json
```

### Passo 2: Criar ECR Repositories (Registry Docker)

```powershell
# Login no ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin [SEU_ACCOUNT_ID].dkr.ecr.us-east-1.amazonaws.com

# Criar repositories
aws ecr create-repository --repository-name beepharma/backend --region us-east-1
aws ecr create-repository --repository-name beepharma/frontend --region us-east-1
```

### Passo 3: Build e Push das Imagens

```powershell
cd c:\Users\Gabriel\Documents\BeeFarma\BeePharma

# Build backend
docker build -t beepharma-backend .
docker tag beepharma-backend:latest [SEU_ACCOUNT_ID].dkr.ecr.us-east-1.amazonaws.com/beepharma/backend:latest
docker push [SEU_ACCOUNT_ID].dkr.ecr.us-east-1.amazonaws.com/beepharma/backend:latest

# Build frontend
docker build -t beepharma-frontend -f Dockerfile.frontend .
docker tag beepharma-frontend:latest [SEU_ACCOUNT_ID].dkr.ecr.us-east-1.amazonaws.com/beepharma/frontend:latest
docker push [SEU_ACCOUNT_ID].dkr.ecr.us-east-1.amazonaws.com/beepharma/frontend:latest
```

### Passo 4: Criar RDS MariaDB

Via AWS Console (mais fácil):
1. Acesse RDS → Create Database
2. Escolha **MariaDB 10.11**
3. Template: **Free Tier** (db.t3.micro)
4. Settings:
   - DB instance: `beepharma-db`
   - Master username: `admin`
   - Master password: [ANOTE BEM ESSA SENHA]
5. Connectivity:
   - VPC: Default
   - Public access: **No**
   - VPC Security Group: Create new `beepharma-db-sg`
6. Additional Config:
   - Initial database name: `beepharma`
   - Backup: 7 days
   - Enable automatic backups

**Custo**: ~$15/mês (db.t3.micro)

### Passo 5: Criar ECS Cluster

```powershell
# Criar cluster
aws ecs create-cluster --cluster-name beepharma-cluster --region us-east-1
```

### Passo 6: Configurar Task Definitions

Vou criar os arquivos de configuração...

---

## 🚀 Opção 2: Deploy com AWS Lightsail (MAIS SIMPLES)

**Custo**: $10-20/mês

### Vantagens:
- ✅ Interface mais simples
- ✅ Preço fixo previsível
- ✅ IP estático incluído
- ✅ Backup automático
- ✅ Ideal para começar

### Passos:

1. **Acesse AWS Lightsail Console**
2. **Create Instance**:
   - Platform: Linux/Unix
   - Blueprint: OS Only → Ubuntu 22.04 LTS
   - Plan: $10/mês (2GB RAM, 1 vCPU)
3. **Launch**

4. **Conecte via SSH e instale**:
```bash
# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Clone seu repositório
git clone https://github.com/lookin-at-the-stars/BeeFarma.git
cd BeeFarma/BeePharma

# Suba a aplicação
docker-compose up -d
```

5. **Configure Networking**:
   - Lightsail → Networking → Add Rule
   - HTTP: Port 80
   - HTTPS: Port 443 (se tiver SSL)
   - Custom: Port 8080 (temporário)

6. **Acesse**: `http://[SEU_IP_LIGHTSAIL]`

---

## 🚀 Opção 3: Deploy com AWS App Runner (MAIS FÁCIL)

**Custo**: ~$20-30/mês

AWS App Runner detecta seu Dockerfile e faz deploy automático!

```powershell
# Instalar copilot CLI (facilita App Runner)
Invoke-WebRequest -OutFile 'C:\Program Files\copilot.exe' https://github.com/aws/copilot-cli/releases/latest/download/copilot-windows.exe

# Inicializar
copilot init

# Seguir prompts:
# - Application name: beepharma
# - Service type: Load Balanced Web Service
# - Service name: backend
# - Dockerfile: ./Dockerfile
```

---

## 🔒 Segurança Essencial

### 1. Security Groups
```bash
# Backend Security Group
- Inbound: Port 8080 from ALB only
- Outbound: All

# Database Security Group  
- Inbound: Port 3306 from Backend SG only
- Outbound: None

# ALB Security Group
- Inbound: Port 80/443 from 0.0.0.0/0
- Outbound: Port 8080 to Backend SG
```

### 2. Secrets Manager
```powershell
# Armazenar senha do DB
aws secretsmanager create-secret `
    --name beepharma/db-password `
    --secret-string "SUA_SENHA_AQUI" `
    --region us-east-1
```

---

## 📊 Comparação das Opções

| Opção | Dificuldade | Custo/Mês | Escalabilidade | Tempo Setup |
|-------|-------------|-----------|----------------|-------------|
| **Lightsail** | ⭐ Fácil | $10-20 | Baixa | 15 min |
| **App Runner** | ⭐⭐ Médio | $20-30 | Média | 20 min |
| **ECS Fargate** | ⭐⭐⭐ Difícil | $30-50 | Alta | 60 min |
| **EC2 Manual** | ⭐⭐⭐⭐ Muito Difícil | $20-40 | Média | 90 min |

---

## 🎯 Minha Recomendação

**Para começar**: AWS Lightsail ($10/mês)
- Simples, rápido, previsível
- Seu crédito dura 10 meses!
- Quando crescer → migre para ECS

**Se quiser "serverless"**: App Runner
- Zero gerenciamento de servidor
- Escala automaticamente

---

## 📝 Próximos Passos

Escolha qual opção prefere e eu te ajudo com:
1. ✅ Arquivos de configuração específicos
2. ✅ Scripts de deploy automatizado
3. ✅ Configuração de domínio
4. ✅ SSL/HTTPS com Let's Encrypt
5. ✅ Monitoramento e logs

**Qual opção você prefere? Lightsail, App Runner ou ECS?** 🐝
