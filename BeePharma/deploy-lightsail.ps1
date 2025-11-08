# 🐝 Script de Deploy Automático - AWS Lightsail
# Execute: .\deploy-lightsail.ps1

Write-Host "🐝 BeePharma - Deploy AWS Lightsail" -ForegroundColor Yellow
Write-Host "====================================`n" -ForegroundColor Yellow

# Verificar se AWS CLI está instalado
if (!(Get-Command aws -ErrorAction SilentlyContinue)) {
    Write-Host "❌ AWS CLI não encontrado!" -ForegroundColor Red
    Write-Host "Instale com: winget install Amazon.AWSCLI" -ForegroundColor Yellow
    exit 1
}

# Configurações
$INSTANCE_NAME = "beepharma-server"
$BLUEPRINT_ID = "ubuntu_22_04"
$BUNDLE_ID = "medium_2_0" # $10/mês - 2GB RAM, 1 vCPU
$REGION = "us-east-1"

Write-Host "📋 Configuração:" -ForegroundColor Cyan
Write-Host "  Instance: $INSTANCE_NAME"
Write-Host "  Plan: `$10/mês (2GB RAM)"
Write-Host "  Region: $REGION`n"

$confirm = Read-Host "Continuar? (s/n)"
if ($confirm -ne 's') {
    Write-Host "Cancelado." -ForegroundColor Yellow
    exit 0
}

# Criar instância Lightsail
Write-Host "`n📦 Criando instância Lightsail..." -ForegroundColor Green
aws lightsail create-instances `
    --instance-names $INSTANCE_NAME `
    --availability-zone "${REGION}a" `
    --blueprint-id $BLUEPRINT_ID `
    --bundle-id $BUNDLE_ID `
    --region $REGION `
    --tags key=Project,value=BeePharma

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erro ao criar instância!" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Instância criada! Aguardando inicialização..." -ForegroundColor Green
Start-Sleep -Seconds 30

# Abrir portas
Write-Host "`n🔓 Configurando firewall..." -ForegroundColor Green
aws lightsail open-instance-public-ports `
    --instance-name $INSTANCE_NAME `
    --port-info fromPort=80,toPort=80,protocol=tcp `
    --region $REGION

aws lightsail open-instance-public-ports `
    --instance-name $INSTANCE_NAME `
    --port-info fromPort=443,toPort=443,protocol=tcp `
    --region $REGION

# Obter IP público
Write-Host "`n🌐 Obtendo IP público..." -ForegroundColor Green
$IP = aws lightsail get-instance `
    --instance-name $INSTANCE_NAME `
    --region $REGION `
    --query 'instance.publicIpAddress' `
    --output text

Write-Host "✅ IP Público: $IP" -ForegroundColor Green

# Criar script de setup
$SETUP_SCRIPT = @"
#!/bin/bash
set -e

echo '🐝 Instalando Docker...'
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu

echo '🐝 Instalando Docker Compose...'
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-\$(uname -s)-\$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

echo '🐝 Clonando repositório...'
cd /home/ubuntu
git clone https://github.com/lookin-at-the-stars/BeeFarma.git
cd BeeFarma/BeePharma

echo '🐝 Ajustando compose.yaml para produção...'
# Remover mapeamento de porta 3306 (segurança)
sed -i '/3306:3306/d' compose.yaml

# Alterar porta do frontend de 80 para 80 (já está ok)
# Ajustar API_BASE_URL no frontend
sed -i 's|http://localhost:8080|http://$IP:8080|g' frontend/app.js

echo '🐝 Iniciando aplicação...'
docker-compose up -d

echo '✅ Deploy concluído!'
echo 'Frontend: http://$IP'
echo 'Backend: http://$IP:8080/api'
"@

$SETUP_SCRIPT | Out-File -FilePath "setup-server.sh" -Encoding ASCII

Write-Host "`n📤 Aguarde ~2 minutos para a instância ficar pronta..." -ForegroundColor Yellow
Write-Host "Depois, conecte via SSH e execute o setup:`n" -ForegroundColor Yellow

Write-Host "# 1. Baixar chave SSH" -ForegroundColor Cyan
Write-Host "aws lightsail download-default-key-pair --region $REGION --output text > lightsail-key.pem`n" -ForegroundColor White

Write-Host "# 2. Conectar via SSH (Linux/Mac)" -ForegroundColor Cyan
Write-Host "chmod 400 lightsail-key.pem" -ForegroundColor White
Write-Host "ssh -i lightsail-key.pem ubuntu@$IP`n" -ForegroundColor White

Write-Host "# 2. Conectar via SSH (Windows)" -ForegroundColor Cyan
Write-Host "Use PuTTY ou:" -ForegroundColor White
Write-Host "ssh -i lightsail-key.pem ubuntu@$IP`n" -ForegroundColor White

Write-Host "# 3. Executar dentro do servidor:" -ForegroundColor Cyan
Write-Host @"
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
sudo usermod -aG docker ubuntu
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-`$(uname -s)-`$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
git clone https://github.com/lookin-at-the-stars/BeeFarma.git
cd BeeFarma/BeePharma
docker-compose up -d
"@ -ForegroundColor White

Write-Host "`n🎉 Após o setup, acesse:" -ForegroundColor Green
Write-Host "   http://$IP" -ForegroundColor Cyan
Write-Host "`n💰 Custo: `$10/mês (seu crédito dura 10 meses!)" -ForegroundColor Yellow
