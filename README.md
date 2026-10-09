# ThinDesk

O ThinDesk é uma aplicação em Java Spring Boot criada para demonstrar cadastro, login, logout e controle de acesso por perfil, o projeto utiliza Thymeleaf na interface e MongoDB Atlas para armazenar usuários e sessões.

## Tecnologias

Java 22, Spring Boot, Spring Security, Thymeleaf, MongoDB Atlas, Spring Data MongoDB, Spring Session e Maven.

## Perfis de acesso

O sistema possui três perfis, `USER`, `MODERATOR` e `ADMIN`, novos usuários cadastrados pela tela pública recebem o perfil `USER`, os demais perfis podem ser definidos diretamente no banco para fins de administração e teste.

De forma geral, `USER` acessa as áreas básicas, `MODERATOR` também pode acessar a área de clientes, `ADMIN` possui acesso às áreas administrativas, o controle é realizado pelo Spring Security e não depende apenas dos botões exibidos na interface.

## Segurança

As senhas não são armazenadas em texto aberto, antes de serem salvas elas passam pelo BCrypt, os campos de cadastro possuem validação de nome, e-mail e senha, as rotas são protegidas conforme o perfil do usuário e as sessões são armazenadas no MongoDB Atlas.

## Estrutura

O projeto foi separado em camadas para facilitar alterações futuras, `controller` recebe as ações das páginas, `service` concentra as regras do sistema, `repository` faz o acesso ao MongoDB, `entity` representa os dados e `templates` contém as páginas Thymeleaf.

Essa separação permite alterar o tema visual ou adaptar o sistema para outro projeto sem precisar modificar toda a lógica de login e segurança.

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

## Banco de dados

Os usuários são armazenados no MongoDB Atlas, as senhas ficam salvas apenas como hash BCrypt, as sessões também são persistidas no MongoDB por meio do Spring Session, a sessão configurada no projeto possui duração de 30 minutos.
