Sistema de Localização Acadêmica (SLA)

Sobre o projeto

O Sistema de Localização Acadêmica (SLA) é uma aplicação web desenvolvida com o objetivo de facilitar o acesso às informações acadêmicas de uma instituição de ensino.

O sistema permite que alunos e professores acessem informações relacionadas às disciplinas, horários e localização das salas, tornando mais simples a consulta dessas informações no ambiente acadêmico.

O projeto foi desenvolvido como trabalho acadêmico, utilizando conceitos de programação Java, integração com banco de dados e desenvolvimento web.

Funcionalidades

- Login de usuários no sistema.
- Identificação dos usuários de acordo com seus perfis.
- Painel de consulta para alunos.
- Painel de consulta para professores.
- Consulta de disciplinas e horários.
- Visualização das informações de alocação de salas.
- Diferenciação entre salas de aulas teóricas e laboratórios para atividades práticas no painel do professor.

Tecnologias utilizadas

- Java 21: linguagem utilizada no desenvolvimento.
- Spring Boot: estrutura principal da aplicação.
- Maven: gerenciamento de dependências e construção do projeto.
- JSF (JavaServer Faces): desenvolvimento das páginas e interfaces.
- JDBC: comunicação entre a aplicação e o banco de dados.
- MySQL: gerenciamento e armazenamento dos dados.
- HTML e XHTML: estruturação das páginas do sistema.

Organização dos pacotes

- controller: contém as classes responsáveis por controlar as ações e interações das páginas.
- dao: contém as classes responsáveis pelo acesso e pelas consultas ao banco de dados.
- model: contém as classes que representam os dados do sistema, como Usuário, Perfil, Sala, Disciplina e Alocação.
- service: contém as regras de negócio da aplicação.
- SlaApplication: classe principal responsável por iniciar o Spring Boot.

Banco de dados

O sistema utiliza um banco de dados MySQL para armazenar as informações necessárias ao funcionamento da aplicação.

As principais tabelas são:

- "perfil": armazena os tipos de perfil dos usuários.
- "usuario": armazena os dados dos usuários.
- "sala": armazena as informações das salas e laboratórios.
- "disciplina": armazena as disciplinas cadastradas.
- "alocacao": relaciona usuários, disciplinas, salas e horários.
