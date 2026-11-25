# ThinDesk

O ThinDesk é uma aplicação de controle de chamados técnicos desenvolvida em Java Spring Boot, o projeto permite o cadastro, login, abertura, acompanhamento e resolução de chamados, utiliza Thymeleaf na interface e MongoDB Atlas para armazenar os dados.

## Tecnologias

Java 21, Spring Boot, Spring Security, Thymeleaf, Bootstrap, MongoDB Atlas, Spring Data MongoDB, Spring Session, Maven e Docker.

## Perfis de acesso

O sistema possui três perfis, `USER`, `MODERATOR` e `ADMIN`, novos usuários cadastrados pela tela pública recebem o perfil `USER`, os demais perfis podem ser definidos diretamente no banco para fins de administração e teste.

O perfil `USER` permite abrir e acompanhar os próprios chamados, o perfil `MODERATOR` representa o técnico, podendo consultar e resolver chamados, o perfil `ADMIN` também possui essas permissões.

## Chamados

Na abertura de um chamado, o usuário informa o título e a descrição, o sistema registra automaticamente o solicitante, a data de abertura e o status `ABERTO`.

Na resolução, o técnico informa a solução aplicada, o sistema registra o responsável, a data de resolução e altera o status para `RESOLVIDO`, chamados resolvidos não podem ser alterados novamente.

## Segurança

As senhas não são armazenadas em texto aberto, antes de serem salvas elas passam pelo BCrypt, os campos de cadastro possuem validação de nome, e-mail e senha, as rotas são protegidas conforme o perfil do usuário e as sessões são armazenadas no MongoDB Atlas.

A identificação do solicitante e do técnico é realizada por meio da conta autenticada no sistema.

## Endpoints

- `POST /api/chamados` — abertura de chamado;
- `GET /api/chamados/usuario/{id}` — listagem dos chamados de um usuário;
- `GET /api/chamados/{id}` — consulta de um chamado;
- `PUT /api/chamados/{id}/resolver` — resolução de chamado.

Os endpoints foram integrados ao sistema de autenticação do ThinDesk, utilizando as sessões e permissões dos usuários.

## Configuração do MongoDB Atlas

O endereço completo do banco não deve ser salvo no GitHub, o arquivo `application.properties` utiliza a variável de ambiente `MONGODB_URI`.

No PowerShell, para definir a variável apenas na janela atual:

```powershell
$env:MONGODB_URI="mongodb+srv://USUARIO:SENHA@SEU-CLUSTER.mongodb.net/?appName=thindesk"
```

Para salvar a variável no Windows:

```powershell
setx MONGODB_URI "mongodb+srv://USUARIO:SENHA@SEU-CLUSTER.mongodb.net/?appName=thindesk"
```

Depois de usar `setx`, feche e abra novamente o terminal ou o NetBeans.

## Como executar

Clone o repositório, abra a pasta que contém o arquivo `pom.xml`, configure a variável `MONGODB_URI` e execute o projeto pelo NetBeans ou pelo Maven Wrapper.

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse:

```text
http://localhost:8080/login
```

## Versionamento

A versão atual é `1.1.0-beta`, o incremento do MINOR representa a inclusão de novas funcionalidades, enquanto a mudança de Alpha para Beta representa a disponibilização do sistema para testes externos.

### 1.0.0-alpha

Versão inicial do ThinDesk, com foco na autenticação e no controle de acesso.

- Cadastro, login e logout;
- Controle de acesso pelos perfis USER, MODERATOR e ADMIN;
- Validação dos dados de cadastro;
- Proteção de senhas com BCrypt;
- Armazenamento de usuários e sessões no MongoDB Atlas;
- Interface desenvolvida com Thymeleaf.

### 1.1.0-beta

Integração das funcionalidades desenvolvidas no microserviço de chamados técnicos ao ThinDesk.

- Integração dos endpoints REST;
- Abertura e acompanhamento de chamados;
- Resolução de chamados pelo técnico;
- Validação das regras de negócio;
- Adaptação da interface original;
- Implementação do dashboard;
- Testes unitários;
- Configuração para deploy utilizando Docker.

## Banco de dados

Os usuários, chamados e sessões são armazenados no MongoDB Atlas, as senhas ficam salvas apenas como hash BCrypt, as sessões são persistidas por meio do Spring Session.

## Dashboard

O dashboard apresenta a quantidade total de chamados, chamados abertos, chamados resolvidos e os registros mais recentes.

![Dashboard do ThinDesk](docs/dashboard.png)

## Deploy

Link da aplicação: PENDENTE

### Usuário de teste

- E-mail: PENDENTE
- Senha: PENDENTE

### Técnico de teste

- E-mail: PENDENTE
- Senha: PENDENTE