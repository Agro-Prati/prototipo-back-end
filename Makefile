# ===================================
# Makefile - AgroMais Prati Backend
# ===================================

.PHONY: help build up down restart logs clean test package run dev db-up db-down db-logs db-clean install status

# Cores para output
GREEN  := \033[0;32m
YELLOW := \033[0;33m
RED    := \033[0;31m
NC     := \033[0m # No Color

# Variáveis
DOCKER_COMPOSE = docker-compose
MVN = ./mvnw
APP_NAME = prototipo-back-agro-spring
JAR_FILE = target/$(APP_NAME)-0.0.1-SNAPSHOT.jar
SPRING_PROFILE = dev

# ===================================
# Help - Mostra todos os comandos
# ===================================
help:
	@echo "$(GREEN)╔════════════════════════════════════════════════════╗$(NC)"
	@echo "$(GREEN)║      AgroMais Prati - Backend Makefile            ║$(NC)"
	@echo "$(GREEN)╚════════════════════════════════════════════════════╝$(NC)"
	@echo ""
	@echo "$(YELLOW)🐳 Docker Commands:$(NC)"
	@echo "  make up              - Sobe todos os containers (DB + Redis + pgAdmin)"
	@echo "  make down            - Para e remove todos os containers"
	@echo "  make restart         - Reinicia todos os containers"
	@echo "  make logs            - Mostra logs dos containers"
	@echo "  make status          - Status dos containers"
	@echo ""
	@echo "$(YELLOW)🗄️  Database Commands:$(NC)"
	@echo "  make db-up           - Sobe apenas o PostgreSQL"
	@echo "  make db-down         - Para apenas o PostgreSQL"
	@echo "  make db-logs         - Logs do PostgreSQL"
	@echo "  make db-clean        - Remove volumes do banco (CUIDADO!)"
	@echo "  make db-shell        - Acessa shell do PostgreSQL"
	@echo ""
	@echo "$(YELLOW)☕ Java/Maven Commands:$(NC)"
	@echo "  make install         - Instala dependências Maven"
	@echo "  make build           - Compila o projeto"
	@echo "  make package         - Gera o JAR"
	@echo "  make test            - Roda os testes"
	@echo "  make run             - Roda a aplicação Spring Boot"
	@echo "  make clean           - Limpa target/ e .class"
	@echo ""
	@echo "$(YELLOW)🚀 Development Commands:$(NC)"
	@echo "  make dev             - Ambiente completo (DB + App rodando)"
	@echo "  make full-restart    - Limpa tudo, reconstrói e sobe"
	@echo "  make check           - Verifica se tudo está funcionando"
	@echo ""
	@echo "$(YELLOW)🧹 Cleanup Commands:$(NC)"
	@echo "  make clean-all       - Remove tudo (containers, volumes, target/)"
	@echo ""

# ===================================
# Docker Commands
# ===================================

# Sobe todos os containers
up:
	@echo "$(GREEN)🐳 Subindo containers...$(NC)"
	$(DOCKER_COMPOSE) up -d
	@echo "$(GREEN)✅ PostgreSQL rodando!$(NC)"
	@echo ""
	@echo "$(YELLOW)📊 Serviços disponíveis:$(NC)"
	@echo "  PostgreSQL:  localhost:5433"
	@echo "  pgAdmin:     http://localhost:8081 (admin@admin.com / admin)"
	@echo ""

# Para e remove todos os containers
down:
	@echo "$(RED)🛑 Parando containers...$(NC)"
	$(DOCKER_COMPOSE) down
	@echo "$(GREEN)✅ Containers parados!$(NC)"

# Reinicia todos os containers
restart:
	@echo "$(YELLOW)🔄 Reiniciando containers...$(NC)"
	$(DOCKER_COMPOSE) restart
	@echo "$(GREEN)✅ Containers reiniciados!$(NC)"

# Mostra logs
logs:
	$(DOCKER_COMPOSE) logs -f

# Status dos containers
status:
	@echo "$(GREEN)📊 Status dos containers:$(NC)"
	@docker ps --filter "name=agro" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# ===================================
# Database Commands
# ===================================

# Sobe apenas PostgreSQL
db-up:
	@echo "$(GREEN)🗄️  Subindo PostgreSQL...$(NC)"
	$(DOCKER_COMPOSE) up -d agro-db
	@echo "$(GREEN)✅ PostgreSQL rodando em localhost:5433$(NC)"

# Para PostgreSQL
db-down:
	@echo "$(RED)🛑 Parando PostgreSQL...$(NC)"
	$(DOCKER_COMPOSE) stop agro-db
	@echo "$(GREEN)✅ PostgreSQL parado!$(NC)"

# Logs do PostgreSQL
db-logs:
	$(DOCKER_COMPOSE) logs -f agro-db

# Remove volumes do banco (CUIDADO!)
db-clean:
	@echo "$(RED)⚠️  ATENÇÃO: Isso vai DELETAR todos os dados do banco!$(NC)"
	@echo "$(RED)Pressione Ctrl+C para cancelar, ou Enter para continuar...$(NC)"
	@read -r
	$(DOCKER_COMPOSE) down -v
	@echo "$(GREEN)✅ Volumes removidos!$(NC)"

# Shell do PostgreSQL
db-shell:
	@echo "$(GREEN)🐘 Acessando PostgreSQL...$(NC)"
	@echo "$(YELLOW)Senha: postgres$(NC)"
	docker exec -it $$(docker ps -qf "name=agro-db") psql -U postgres -d agro_prototipo_db

# ===================================
# Java/Maven Commands
# ===================================

# Instala dependências
install:
	@echo "$(GREEN)📦 Instalando dependências Maven...$(NC)"
	$(MVN) clean install -DskipTests
	@echo "$(GREEN)✅ Dependências instaladas!$(NC)"

# Compila o projeto
build:
	@echo "$(GREEN)🔨 Compilando projeto...$(NC)"
	$(MVN) clean compile
	@echo "$(GREEN)✅ Projeto compilado!$(NC)"

# Gera o JAR
package:
	@echo "$(GREEN)📦 Gerando JAR...$(NC)"
	$(MVN) clean package -DskipTests
	@echo "$(GREEN)✅ JAR gerado: $(JAR_FILE)$(NC)"

# Roda testes
test:
	@echo "$(GREEN)🧪 Rodando testes...$(NC)"
	$(MVN) test
	@echo "$(GREEN)✅ Testes concluídos!$(NC)"

# Roda a aplicação
run:
	@echo "$(GREEN)🚀 Iniciando aplicação Spring Boot...$(NC)"
	$(MVN) spring-boot:run -Dspring-boot.run.profiles=$(SPRING_PROFILE)

# Limpa arquivos compilados
clean:
	@echo "$(YELLOW)🧹 Limpando arquivos compilados...$(NC)"
	$(MVN) clean
	@echo "$(GREEN)✅ Limpeza concluída!$(NC)"

# ===================================
# Development Commands
# ===================================

# Ambiente de desenvolvimento completo
dev: db-up
	@echo "$(GREEN)🚀 Iniciando ambiente de desenvolvimento...$(NC)"
	@echo "$(YELLOW)⏳ Aguardando PostgreSQL inicializar (5s)...$(NC)"
	@sleep 5
	@echo "$(GREEN)✅ PostgreSQL pronto!$(NC)"
	@echo ""
	@echo "$(YELLOW)🔨 Compilando e rodando aplicação...$(NC)"
	$(MVN) spring-boot:run -Dspring-boot.run.profiles=$(SPRING_PROFILE)

# Restart completo (limpa tudo e reconstrói)
full-restart: clean-all install up
	@echo "$(GREEN)✨ Ambiente completamente reconstruído!$(NC)"
	@echo ""
	@echo "$(YELLOW)Para iniciar a aplicação, rode:$(NC)"
	@echo "  make run"

# Verifica se tudo está OK
check:
	@echo "$(GREEN)🔍 Verificando ambiente...$(NC)"
	@echo ""
	@echo "$(YELLOW)📊 Containers Docker:$(NC)"
	@docker ps --filter "name=agro" --format "  ✓ {{.Names}} - {{.Status}}"
	@echo ""
	@echo "$(YELLOW)☕ Java Version:$(NC)"
	@java -version 2>&1 | head -n 1 | sed 's/^/  ✓ /'
	@echo ""
	@echo "$(YELLOW)📦 Maven Version:$(NC)"
	@$(MVN) -version | head -n 1 | sed 's/^/  ✓ /'
	@echo ""
	@echo "$(YELLOW)🗄️  PostgreSQL Connection:$(NC)"
	@if docker exec $$(docker ps -qf "name=agro-db") pg_isready -U postgres > /dev/null 2>&1; then \
		echo "  ✓ PostgreSQL está pronto!"; \
	else \
		echo "  ✗ PostgreSQL não está respondendo"; \
	fi
	@echo ""
	@if [ -f $(JAR_FILE) ]; then \
		echo "$(YELLOW)📦 JAR File:$(NC)"; \
		echo "  ✓ $(JAR_FILE) existe"; \
	else \
		echo "$(YELLOW)📦 JAR File:$(NC)"; \
		echo "  ⚠️  JAR não encontrado. Rode 'make package'"; \
	fi

# ===================================
# Cleanup Commands
# ===================================

# Remove tudo
clean-all: down
	@echo "$(RED)🧹 Removendo tudo...$(NC)"
	$(DOCKER_COMPOSE) down -v
	$(MVN) clean
	@rm -rf target/
	@echo "$(GREEN)✅ Limpeza completa!$(NC)"

# ===================================
# Atalhos úteis
# ===================================

# Aliases
start: up
stop: down
rebuild: full-restart
