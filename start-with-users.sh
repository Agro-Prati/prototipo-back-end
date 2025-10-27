#!/bin/bash

# Script para iniciar a aplicação e verificar a criação de usuários

echo "🚀 Iniciando aplicação Spring Boot..."
echo "📊 Aguarde a inicialização do banco de dados e da aplicação..."
echo ""

cd /home/leandro/Documentos/projeto-final-agromaisprati/prototipo-back-end

# Inicia o banco de dados se não estiver rodando
if ! docker ps | grep -q agro-db; then
    echo "🐘 Iniciando PostgreSQL..."
    make db-up
    sleep 5
fi

# Inicia a aplicação
./mvnw spring-boot:run &
APP_PID=$!

# Aguarda logs de inicialização
sleep 10

# Exibe os logs relevantes
echo ""
echo "📝 Logs de criação de usuários:"
echo "======================================"
tail -f target/spring-boot.log 2>/dev/null | grep -E "Criando usuários|Criado usuário|Usuários de exemplo" &
TAIL_PID=$!

# Aguarda um pouco para ver os logs
sleep 5

# Para os processos
kill $TAIL_PID 2>/dev/null
kill $APP_PID 2>/dev/null

echo ""
echo "======================================"
echo "✅ Aplicação iniciada!"
echo ""
echo "👥 Usuários criados:"
echo "  - agricultor@agro.com (AGRICULTOR)"
echo "  - agronomo@agro.com (AGRONOMO)"
echo "  - veterinaria@agro.com (VETERINARIO)"
echo "  - zootecnista@agro.com (ZOOTECNISTA)"
echo "  - estudante@agro.com (ESTUDANTE)"
echo ""
echo "🔑 Senha para todos: senha123"
echo ""
echo "Para iniciar manualmente:"
echo "  cd /home/leandro/Documentos/projeto-final-agromaisprati/prototipo-back-end"
echo "  ./mvnw spring-boot:run"
