# Protótipo Back-End - AgroPrati (Spring Boot)

## 📋 Instruções para Desenvolvimento

Este repositório contém o protótipo back-end da plataforma AgroPrati, desenvolvido com Spring Boot e Java 17. Este README fornece instruções técnicas para a equipe de desenvolvimento.

## ⚙️ Configuração do Ambiente

### Pré-requisitos
- **Java 17 (JDK)** - Obrigatório
- **Apache Maven 3.8+** - Para gerenciamento de dependências
- **Git** - Para controle de versão
- **IDE recomendada**: IntelliJ IDEA, Eclipse ou VS Code com extensão Java
- **Postman ou Insomnia** - Para testar APIs (opcional)

### Verificar Versões Instaladas
```bash
# Verificar versão do Java (deve ser 17)
java -version

# Verificar versão do Maven
mvn -version

# Verificar Git
git --version
```

## 🚀 Primeiros Passos

### 1. Clonar o Repositório
```bash
git clone git@github.com:Agro-Prati/prototipo-back-end.git

# Entre na pasta do projeto
cd prototipo-back-agro-spring
```

### 2. Configurar Git (caso ainda não tenha feito)
```bash
# Configure seu nome (substitua pelo seu nome)
git config --global user.name "Seu Nome"

# Configure seu email (substitua pelo seu email)
git config --global user.email "seu.email@exemplo.com"
```

### 3. Instalar Dependências e Compilar
```bash
# Baixar dependências e compilar o projeto
./mvnw clean install

# Ou se tiver Maven instalado globalmente:
mvn clean install
```

## 🌿 Fluxo de Desenvolvimento

### 4. Criar uma Branch de Desenvolvimento

**IMPORTANTE:** Nunca trabalhe diretamente na branch `main`. Sempre crie uma nova branch para suas modificações.

```bash
# Certifique-se de estar na branch main atualizada
git checkout main
git pull origin main

# Crie sua branch de desenvolvimento
git checkout -b nome-da-sua-feature

# Exemplos de nomes de branch:
# git checkout -b adicionar-endpoint-usuarios
# git checkout -b implementar-autenticacao
# git checkout -b corrigir-bug-validacao
```

### 5. Executar o Projeto

```bash
# Executar a aplicação Spring Boot
./mvnw spring-boot:run

# Ou com Maven global:
mvn spring-boot:run

# A aplicação estará disponível em: http://localhost:8080
```

### 6. Fazer Alterações no Código

- Implemente suas funcionalidades nos packages apropriados
- Execute testes para garantir que suas alterações funcionam
- Teste endpoints usando Postman/Insomnia ou curl
- Certifique-se de que a aplicação inicia sem erros

### 7. Executar Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes com relatório detalhado
./mvnw test -Dtest.verbose=true

# Executar um teste específico
./mvnw test -Dtest=NomeDaClasseTest
```

### 8. Salvar suas Alterações (Commit)

```bash
# Veja quais arquivos foram modificados
git status

# Adicione os arquivos modificados ao staging area
git add .
# Ou adicione arquivos específicos:
# git add src/main/java/com/exemplo/MinhaClasse.java

# Faça o commit com uma mensagem descritiva
git commit -m "Descrição clara do que foi alterado"

# Exemplos de mensagens de commit:
# git commit -m "Adicionar endpoint GET /api/usuarios"
# git commit -m "Implementar validação de email no cadastro"
# git commit -m "Corrigir bug na autenticação JWT"
```

### 9. Enviar para o Repositório Remoto

```bash
# Envie sua branch para o repositório remoto
git push origin nome-da-sua-feature
```

### 10. Criar Pull Request

1. Acesse o repositório no GitHub: https://github.com/Agro-Prati/prototipo-back-end
2. Você verá uma notificação para criar Pull Request da sua branch
3. Clique em "Compare & pull request"
4. Preencha:
   - **Título**: Descrição clara do que foi implementado
   - **Descrição**: Detalhes sobre as mudanças, endpoints alterados, etc.
5. Clique em "Create pull request"
6. Aguarde a revisão do líder da equipe

## 🔄 Mantendo sua Branch Atualizada

```bash
# Mude para a branch main
git checkout main

# Baixe as últimas atualizações
git pull origin main

# Volte para sua branch de desenvolvimento
git checkout nome-da-sua-feature

# Atualize sua branch com as mudanças da main
git merge main
```

## 📁 Estrutura do Projeto

```
prototipo-back-agro-spring/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/agromaisprati/prototipobackagrospring/
│   │   │       ├── PrototipoBackAgroSpringApplication.java
│   │   │       ├── controller/     # Controllers REST
│   │   │       ├── service/        # Lógica de negócio
│   │   │       ├── model/          # Entidades/DTOs
│   │   │       ├── repository/     # Acesso a dados
│   │   │       └── config/         # Configurações
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/             # Arquivos estáticos
│   │       └── templates/          # Templates (se usar)
│   └── test/
│       └── java/                   # Testes unitários
├── target/                         # Arquivos compilados
├── pom.xml                         # Dependências Maven
├── mvnw                            # Maven Wrapper
└── README.md                       # Este arquivo
```

## 🛠️ Comandos Maven Úteis

```bash
# Limpar e compilar
./mvnw clean compile

# Executar aplicação
./mvnw spring-boot:run

# Executar testes
./mvnw test

# Gerar JAR
./mvnw clean package

# Executar JAR gerado
java -jar target/prototipo-back-agro-spring-0.0.1-SNAPSHOT.jar

# Verificar dependências desatualizadas
./mvnw versions:display-dependency-updates
```

## ❗ Comandos Git Importantes para Lembrar

```bash
# Ver status dos arquivos
git status

# Ver histórico de commits
git log --oneline

# Ver diferenças nos arquivos modificados
git diff

# Voltar alterações não commitadas
git checkout -- nome-do-arquivo

# Ver todas as branches
git branch -a

# Trocar de branch
git checkout nome-da-branch
```

## 🆘 Problemas Comuns

### Erro "JAVA_HOME not set"
```bash
# Linux/Mac - adicione ao ~/.bashrc ou ~/.zshrc
export JAVA_HOME=/path/to/java17
export PATH=$JAVA_HOME/bin:$PATH

# Windows - configure nas variáveis de ambiente do sistema
```

### Erro de permissão no mvnw
```bash
# Dar permissão de execução ao Maven Wrapper
chmod +x mvnw
```

### Porta 8080 ocupada
```bash
# Alterar porta no application.properties
server.port=8081

# Ou definir via variável de ambiente
export SERVER_PORT=8081
```

### Conflitos de merge
- Se houver conflitos, abra os arquivos marcados
- Resolva os conflitos manualmente
- Adicione os arquivos resolvidos: `git add .`
- Complete o merge: `git commit`

### Erro ao fazer push pela primeira vez
```bash
# Se aparecer erro na primeira vez, use:
git push -u origin nome-da-sua-branch
```

## 🧪 Testando Endpoints

### Exemplos com curl
```bash
# Testar se aplicação está rodando
curl http://localhost:8080/actuator/health

# Exemplo de GET
curl -X GET http://localhost:8080/api/usuarios

# Exemplo de POST
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome": "João", "email": "joao@email.com"}'
```

## 📞 Contato

Em caso de dúvidas, entre em contato com o líder da equipe ou consulte a documentação do Spring Boot.

## 🎯 Dicas Importantes

### ✅ Boas Práticas
- Sempre execute testes antes de fazer commit
- Use mensagens de commit claras e descritivas
- Mantenha sua branch atualizada com a main
- Faça commits pequenos e frequentes
- Documente APIs com comentários ou Swagger
- Siga convenções de nomenclatura Java (camelCase, PascalCase)
- Use anotações Spring adequadas (@RestController, @Service, etc.)

### ⚠️ Cuidados
- Evite commits muito grandes; prefira dividir em partes menores
- Não commite arquivos de configuração pessoal (application-local.properties)
- Valide dados de entrada nos endpoints
- Trate exceções adequadamente

### ❌ Nunca Faça
- Trabalhar diretamente na branch main
- Commits com código que não compila
- Push de arquivos target/ ou .class
- Hardcode de senhas ou tokens no código
- Ignorar warnings de segurança