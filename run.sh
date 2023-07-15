#!/bin/bash

if ! command -v mvn &> /dev/null; then
  echo "Erro: Maven não encontrado. Certifique-se de ter o Maven instalado no seu sistema."
  exit 1
fi

if [ "$1" = "-test" ]; then
  mvn test
else
  cd target/ && nohup java -jar management-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &
fi