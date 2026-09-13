# 🏛️ Sistema Corporativo de Reserva de Salas de Reunião

Este projeto foi desenvolvido como solução para a **Atividade Substitutiva (Fase 1)**. Trata-se de uma API RESTful de alta performance projetada sob os princípios do **Domain-Driven Design (DDD)** para gerenciar o agendamento de espaços físicos corporativos, catálogos de recursos e controle de segurança baseado em papéis (RBAC).

---

## 🛠️ Tecnologias Utilizadas

*   **Java 21 (LTS)** – Uso de recursos modernos e otimização de concorrência.
*   **Spring Boot 4.1.1** – Ecossistema base (Spring Data JPA, Spring Security).
*   **MySQL 8.0** – Banco de dados relacional para persistência de dados.
*   **JSON Web Tokens (JWT) via JJWT 0.13** – Autenticação e autorização *stateless*.
*   **Resend SDK** – Infraestrutura para envio assíncrono de e-mails transacionais.
*   **Docker & Docker Compose** – Conteinerização e portabilidade do ambiente.

---

## 🏗️ Arquitetura do Projeto (DDD)

A estrutura de pacotes segue a separação rígida de responsabilidades, mantendo o coração do negócio (o Domínio) isolado de acoplamentos tecnológicos:

*   `domain`: Entidades ricas com auto-validação, Objetos de Valor (`Periodo`), eventos e contratos de repositórios.
*   `application`: Casos de Uso (`UseCases`) focados, responsáveis pela orquestração do fluxo do sistema.
*   `infrastructure`: Adaptadores de entrada e saída (Controladores REST, filtros de segurança JWT, ouvinte do Resend e tratamento global de erros).

---

## 🚀 Como Executar a Aplicação (Passo a Passo)

Certifique-se de ter o **Git** e o **Docker Desktop** instalados e rodando em sua máquina.

**1. Clonar o Repositório:**
```bash
git clone https://github.com/joaopassone/reserva-sala-reuniao.git
```

**2. Acessar o Diretório Raiz:**
```bash
cd reserva-sala-reuniao
```

**3. Configurar Variáveis do Resend (E-mail):**
Abra o arquivo `src/main/resources/application.properties` e adicione sua chave privada e o e-mail de remetente homologado:
```properties
resend.api.key=copiar_chave_relatório
```
*(Opcional)*: Altere o e-mail do administrador padrão no arquivo `src/main/resources/data.sql` para o seu e-mail pessoal de testes para receber os disparos reais de agendamento.

**4. Subir a Infraestrutura:**
Execute o comando do Docker Compose para realizar o *multi-stage build* do Java 21 e inicializar o MySQL 8.0:
```bash
docker compose up --build
```
A API estará pronta para o uso assim que os logs confirmarem a inicialização do Spring Boot na porta **`8080`**. O banco de dados será populado automaticamente com uma massa de dados inicial de testes (`data.sql`).

---

## 🧪 Roteiro de Testes no Postman

O repositório inclui o arquivo de coleção oficial do Postman para validação do sistema. 

### Como Importar e Testar:
1. Abra o Postman e clique em **Import**.
2. Selecione o arquivo de configuração contido na raiz do projeto.
3. Execute as requisições **seguindo a ordem exata em que aparecem na coleção**, pois os scripts internos em JavaScript capturam os Tokens JWT e IDs gerados dinamicamente e os injetam de forma automática nos testes subsequentes.

### Fluxos Validados na Coleção:
*   **Testes de Perímetro:** Bloqueio de requisições anônimas em rotas protegidas (HTTP 401).
*   **Controle RBAC:** Bloqueio de usuários de perfil `COMUM` ao tentar gerenciar salas ou recursos exclusivos de administradores (`GESTOR`) (HTTP 403).
*   **Regra de Ouro (DDD):** Validação e rejeição automática de agendamentos concorrentes ou choques de horários na mesma sala (HTTP 403).
*   **Notificações Reativas:** Disparo assíncrono em segundo plano (`@Async`) de e-mails transacionais via Resend a cada alteração ou cancelamento de status da reserva.
