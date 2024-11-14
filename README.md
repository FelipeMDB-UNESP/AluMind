# AluMind

**AluMind** é uma startup que desenvolve um aplicativo inovador focado em bem-estar e saúde mental. A aplicação oferece aos usuários acesso a meditações guiadas, sessões de terapia e conteúdos educativos sobre saúde mental, visando promover o equilíbrio emocional e melhorar a qualidade de vida.

## IDE RECOMENDADA
   ###  IntelliJ IDEA

## Estrutura do Projeto

O projeto está organizado da seguinte forma:

- **`src/`**: Código-fonte principal do projeto.
- **`tests/`**: Pasta para testes automatizados
- **`collections/`**: Pasta para as requisições HTTP.

## Documentação - Importante!
A documentação para esse projeto pode ser encontrada no [GITHUB WIKI](https://github.com/FelipeMDB-UNESP/AluMind/wiki)

## Tecnologias Utilizadas

- **[Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)** - Linguagem de programação principal.
- **[Spring Boot](https://spring.io/projects/spring-boot)** - Framework para criação de aplicações Spring standalone.
- **[Spring MVC](https://spring.io/guides/gs/serving-web-content/)** - Framework para a criação de APIs e aplicações web.
- **[Lombok](https://projectlombok.org/)** - Biblioteca para reduzir o código boilerplate.
- **[Maven](https://maven.apache.org/download.cgi)** - Ferramenta de gerenciamento e automação de builds.
- **[Amazon RDS](https://aws.amazon.com/rds/)** - Serviço para gerenciamento do banco de dados PostgreSQL na nuvem.
- **[Azure OpenAI](https://www.google.com/aclk?sa=l&ai=DChcSEwjqztHSr9uJAxWcRUgAHTa_Bm4YABABGgJjZQ&co=1&ase=2&gclid=CjwKCAiA3Na5BhAZEiwAzrfagH6avv0Y_WljkODXOfUQ8zFk21eMEAsU9sJDYgeiI9PruboRUHsHZBoC0o4QAvD_BwE&ei=XLA1Z8SqLdDd1sQPvqeyqQI&sig=AOD64_3IEO4eEfLIKFJVOQovbMeQQi2p1g&q&sqi=2&nis=4&adurl&ved=2ahUKEwjEyMzSr9uJAxXQrpUCHb6TLCUQ0Qx6BAgKEAE)** - Serviço para integração com modelos de IA avançados.


## Instalação e Execução

Siga as instruções abaixo para configurar o ambiente e rodar o projeto localmente:

1. **Clonar o repositório**

   Clone o repositório usando Git:

   ```bash
   git clone https://github.com/FelipeMDB-UNESP/AluMind.git

2. **Acesse o repositório no drive do Google**

   Acesse o [repositório no Google Drive](https://drive.google.com/drive/folders/15DGaThqFR3eICXQUY0SiWljGDg7M8RRV?usp=sharing) para obter as credenciais de acesso ao Banco de Dados Amazon AWS e Azure OpenAI.


3. **Substitua por completo o conteúdo do arquivo `application.properties`.**


4. **Navegue até o diretório do projeto e instale as dependências do Maven caso possua mvn instalado localmente:**

   ```bash
   mvn clean install
   ```
   **Ou utilize o mvn de sua IDE**



5. **Execute o projeto:**

   **Acesse o arquivo AlumindApplication.java e execute a aplicação. O projeto estará disponível na porta:**
      ```
   localhost:3000
   ```


6. **Utilize Requisições do Postman(por exemplo) para testar as rotas da API.**

   Uma coleção foi criada previamente para facilitar seus testes! Aproveite!


7. **Em referência à funcionalidade extra.**

   Foi implementada como parte do endpoint já existente para gerar um ciclo completo para os feedbacks.
   A probabilidade desse endpoint ter de lidar com escalabilidade ou aumento de funções é muito pequena.
   Ser responsável por receber, interpretar e devolver a resposta de um feedback é uma funcionalidade que não se encaixa em muitos contextos.
   Logo, a necessidade de reusabilidade diminui e a simplicidade e praticidade de se implementar o algoritmo no mesmo endpoint fica mais evidente!