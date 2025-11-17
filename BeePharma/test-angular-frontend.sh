#!/bin/bash

# 🐝 BeePharma - Script de Teste do Frontend Angular

echo "🐝 BeePharma - Testando Frontend Angular"
echo "=========================================="
echo ""

# Verificar se está no diretório correto
if [ ! -f "compose-angular.yaml" ]; then
    echo "❌ Erro: Execute este script no diretório BeePharma/"
    exit 1
fi

echo "📦 Passo 1: Parando containers antigos..."
docker-compose -f compose-angular.yaml down 2>/dev/null
docker-compose -f compose.yaml down 2>/dev/null

echo ""
echo "🔨 Passo 2: Buildando aplicação Angular..."
cd frontend-angular
npm install
npm run build
cd ..

echo ""
echo "🐳 Passo 3: Buildando imagens Docker..."
docker-compose -f compose-angular.yaml build

echo ""
echo "🚀 Passo 4: Subindo containers..."
docker-compose -f compose-angular.yaml up -d

echo ""
echo "⏳ Aguardando serviços iniciarem (30 segundos)..."
sleep 30

echo ""
echo "✅ Aplicação iniciada!"
echo ""
echo "📍 URLs de Acesso:"
echo "   Frontend Angular: http://localhost"
echo "   Backend API:      http://localhost:8080"
echo "   Swagger:          http://localhost:8080/swagger-ui.html"
echo ""
echo "📊 Status dos containers:"
docker-compose -f compose-angular.yaml ps

echo ""
echo "📝 Para ver os logs:"
echo "   docker-compose -f compose-angular.yaml logs -f"
echo ""
echo "🛑 Para parar:"
echo "   docker-compose -f compose-angular.yaml down"
echo ""
echo "🎉 Frontend Angular está rodando!"
