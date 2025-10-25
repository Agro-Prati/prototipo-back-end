#!/bin/bash

# Carrega variáveis de ambiente do arquivo .env
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

# Inicia o backend
./mvnw spring-boot:run
