# 🚀 Deploy Rápido - AWS Lightsail

## Opção A: Via Script Automatizado (Windows)

```powershell
# 1. Configure AWS CLI primeiro
aws configure
# Cole suas credenciais da AWS

# 2. Execute o script
.\deploy-lightsail.ps1
```

## Opção B: Manual via Console AWS (Mais Simples!)

### 1️⃣ Criar Instância Lightsail

1. Acesse: https://lightsail.aws.amazon.com/
2. **Create instance**
3. Configurações:
   - **Instance location**: us-east-1 (Virginia)
   - **Platform**: Linux/Unix
   - **Blueprint**: OS Only → **Ubuntu 22.04 LTS**
   - **Instance plan**: **$10/month** (2GB RAM, 1 vCPU, 60GB SSD)
   - **Instance name**: `beepharma`
4. Click **Create instance**

### 2️⃣ Configurar Firewall

1. Na instância → **Networking** tab
2. **Add rule**:
   - Application: HTTP
   - Protocol: TCP
   - Port: 80
3. **Add rule**:
   - Application: HTTPS  
   - Protocol: TCP
   - Port: 443
4. Salvar

### 3️⃣ Conectar e Instalar

1. Click **Connect using SSH** (abre terminal no navegador)
2. Cole e execute estes comandos:

```bash
# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu

# Instalar Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Fazer logout e login novamente para aplicar grupo docker
exit
```

**Conecte novamente via SSH e continue:**

```bash
# Clonar repositório
git clone https://github.com/lookin-at-the-stars/BeeFarma.git
cd BeeFarma/BeePharma

# Definir senhas seguras
export DB_PASSWORD="SuaSenhaSegura123!"
export DB_ROOT_PASSWORD="OutraSenhaSegura456!"

# Criar arquivo .env
cat > .env << EOF
DB_PASSWORD=$DB_PASSWORD
DB_ROOT_PASSWORD=$DB_ROOT_PASSWORD
EOF

# Iniciar aplicação
docker-compose -f compose.production.yaml up -d

# Verificar se subiu
docker-compose ps
```

### 4️⃣ Verificar

```bash
# Ver logs
docker-compose logs -f app

# Testar API
curl http://localhost:8080/api/produtos
```

### 5️⃣ Acessar

- Volte no console Lightsail
- Copie o **Public IP** da instância
- Acesse no navegador: `http://[SEU_IP]`

---

## 🔐 Backup Automático

No console Lightsail:
1. Instance → **Snapshots** tab
2. **Create snapshot**
3. **Enable automatic snapshots**

---

## 📊 Monitoramento

No console Lightsail → **Metrics** tab:
- CPU usage
- Network in/out
- Status checks

---

## 🆘 Troubleshooting

### Erro ao conectar no frontend?
```bash
# Verificar se containers estão rodando
docker-compose ps

# Reiniciar
docker-compose restart
```

### Erro no banco de dados?
```bash
# Ver logs do MariaDB
docker-compose logs mariadb

# Entrar no container
docker exec -it beepharma-mariadb-1 mariadb -uroot -p
# Digite a senha root

# Verificar banco
SHOW DATABASES;
USE beepharma;
SHOW TABLES;
```

### Aplicação lenta?
```bash
# Aumentar memória do Docker (se disponível)
# Ou upgrade do plano Lightsail para $20/mês (4GB RAM)
```

---

## 💰 Custos com seu Crédito

- **Plan $10/mês** = 10 meses de uso grátis
- **Plan $20/mês** = 5 meses de uso grátis

---

## 🎯 Próximos Passos

1. ✅ Configurar domínio próprio
2. ✅ Adicionar SSL/HTTPS (Let's Encrypt gratuito)
3. ✅ Configurar backups automáticos
4. ✅ Adicionar monitoramento (CloudWatch)

**Precisa de ajuda com algum desses? Me avise!** 🐝
