# Seia - API (Backend)

O Seia é uma aplicação web desenvolvida para apoiar professores na criação de Planos de Apoio Pedagógico Individualizados. O sistema utiliza a inteligência artificial do Google Gemini para gerar estratégias, adaptações curriculares e metas claras baseadas nas barreiras e necessidades específicas de cada aluno ou turma.

## 🚀 Tecnologias Utilizadas

* **Java / Spring Boot:** Estrutura principal da API REST.
* **PostgreSQL:** Banco de dados relacional.
* **Spring Security & JWT:** Autenticação e proteção de rotas (controle de acesso para perfis ADMIN e PROFESSOR).
* **Spring AI:** Integração direta com a API do Google Gemini (modelo Gemini 2.5 Flash).
* **Flyway:** Versionamento estruturado do banco de dados (Migrations).

## ⚙️ Como Configurar e Rodar o Projeto

### 1. Pré-requisitos
* Java (JDK 17 ou superior).
* PostgreSQL instalado e rodando na porta padrão `5432`.
* Um banco de dados criado com o nome `seia`.
* Uma chave de API válida do Google Gemini (obtida no Google AI Studio).

### 2. Configurando o Ambiente (.env)
Este projeto protege informações sensíveis utilizando variáveis de ambiente. Para rodar a aplicação localmente, crie um arquivo chamado `.env` na raiz do projeto (ou configure as variáveis diretamente nas configurações de execução da sua IDE) com a seguinte estrutura:

```env
# Credenciais do Banco de Dados PostgreSQL
DB_USER=seu_usuario_do_postgres
DB_PASSWORD=sua_senha_do_postgres

# Chave da API do Google Gemini
GEMINI_API_KEY=sua_chave_api_do_google_aqui

# Chave secreta para geração e validação dos tokens JWT (Use uma string complexa)
TOKEN_SECRET=sua_chave_secreta_jwt_aqui
Nota: O arquivo application.properties já está configurado para puxar esses valores dinamicamente através da sintaxe ${NOME_DA_VARIAVEL}.
```

### 3. Rodando a Aplicação
* Como a configuração spring.jpa.hibernate.ddl-auto=validate está ativa, as tabelas não são geradas automaticamente pelas entidades. O projeto utiliza o Flyway para gerenciar a estrutura do banco.

Para iniciar o servidor, execute o projeto pela sua IDE ou utilize o Maven no terminal:

```Bash
mvn spring-boot:run
```
(Para usuários de Linux, você pode usar o wrapper do maven: ./mvnw spring-boot:run)

* A API estará disponível em http://localhost:8080.

🔐 Autenticação e Acesso
* A API é protegida e exige autenticação via Token JWT para o uso de seus recursos principais.

* Cadastro Padrão: O primeiro acesso pode ser feito pela rota pública POST /api/auth/registrar. Por segurança, todos os novos usuários criados via API recebem o perfil padrão de PROFESSOR.

* Acesso Master (ADMIN): Para obter privilégios totais (como excluir registros), o perfil do usuário deve ser promovido para ADMIN diretamente via script de banco de dados (UPDATE usuarios SET role = 'ADMIN' WHERE email = '...';).

* Login: Realizado via POST /api/auth/login, que retorna o token JWT. Este token deve ser enviado no cabeçalho (Header) Authorization: Bearer <token> nas requisições subsequentes.