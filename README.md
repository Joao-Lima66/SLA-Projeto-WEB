- Sistema de Localização Acadêmica (SLA)

Este é o Sistema de Localização Acadêmica, um projeto prático desenvolvido para a faculdade com o objetivo de facilitar o gerenciamento de alocação de salas, professores e disciplinas. Construímos a aplicação seguindo a arquitetura MVC (Model-View-Controller), garantindo que a interface do usuário, as regras de negócio e o banco de dados ficassem muito bem separados e organizados.

- Tecnologias que usamos

O sistema foi todo programado em Java 17 utilizando o framework Spring Boot na versão 3.3.5. 

Para a interface visual, escolhemos o JSF (JavaServer Faces) integrado através do JoinFaces. O armazenamento dos dados é feito no MySQL, e a comunicação direta do Java com o banco de dados foi construída com Spring Data JDBC (usando JdbcTemplate). Todo o gerenciamento de dependências do projeto é feito pelo Maven.

- Como a nossa arquitetura funciona

O código está estruturado em camadas claras e com responsabilidades únicas:

A camada Model abriga as classes como Usuario, Sala, Disciplina e Alocacao, que são o espelho exato das nossas tabelas do banco de dados dentro do Java.

O DAO (Data Access Object) é quem faz o trabalho de executar os comandos SQL diretamente no MySQL e converter as respostas brutas em objetos que o sistema entenda.

A camada Service funciona como a gerente das regras de negócio, validando ações como o login e coordenando os pedidos antes de enviá-los ao DAO.

Os Controllers (nossos Beans) recebem as ações e cliques das telas, processam o que o usuário pediu e devolvem a resposta visual correta.

A View reúne as nossas telas interativas, todas construídas no formato .xhtml.

- Passo a passo para rodar o projeto

1. Configurando o Banco de Dados:
Abra o seu gerenciador do MySQL, como o MySQL Workbench. Importe e rode o arquivo script_banco.sql que está na pasta principal do nosso projeto. Isso vai criar o banco sla_db e inserir os dados iniciais. Se o seu servidor MySQL usar senha, lembre-se de colocá-la no arquivo application.properties (dentro da pasta src/main/resources), preenchendo a linha spring.datasource.password.

2. Ligando a Aplicação:
Abra o terminal na pasta raiz do projeto e execute o comando do Maven Wrapper:
.\mvnw clean spring-boot:run

3. Acessando:
Assim que o terminal exibir que o sistema iniciou, abra o seu navegador e acesse http://localhost:8080 para ver a tela de login.

- Nossa Equipe

João Guilherme: Desenvolvimento do Backend (Classes DAO, Service e Model) e configuração inicial da estrutura do Spring Boot.

Guilherme Almeida: Criação e modelagem do banco de dados no MySQL e desenvolvimento do script de inicialização, e o desenvolvimento das telas Frontend em JSF e integração da interface visual com o backend.

Anna Beatriz: Documentação do projeto, testes de integração e auxílio na configuração do ambiente de desenvolvimento.
