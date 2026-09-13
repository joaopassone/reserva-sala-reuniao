# Estágio 1: Compilação da aplicação usando Java 21 e Maven 3.9
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o arquivo de definição de dependências e os fontes
COPY pom.xml .
COPY src ./src

# Executa o build limpando e empacotando o projeto (pulando os testes para acelerar o processo)
RUN mvn clean package -DskipTests

# Estágio 2: Ambiente de Execução (Apenas o JRE 21, mais leve e seguro)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o artefato compilado do primeiro estágio
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização do contêiner da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
