# Sistema de Gerenciamento de Eventos em Console

## Autor
- **Nome:** Júlio C.
- **Contexto:** Projeto desenvolvido para a disciplina de Soluções Computacionais da Universidade Anhembi Morumbi.
- **Repositório:** [https://github.com/ySozun0/projeto-sistema-eventos](https://github.com/ySozun0/projeto-sistema-eventos)
- **Data de Conclusão:** 31 de Maio de 2025

## Descrição
Este projeto consiste em um sistema de gerenciamento de eventos desenvolvido em Java e executado via interface de console. O sistema permite o cadastro de usuários, criação e gerenciamento de eventos, consulta de eventos disponíveis, confirmação de participação em eventos, e visualização de eventos confirmados pelo usuário. Adicionalmente, o sistema é capaz de ordenar eventos por proximidade temporal, identificar eventos que estão ocorrendo no momento, listar eventos passados e persistir os dados dos eventos em um arquivo local (`events.data`).

## Funcionalidades Principais Implementadas
- **Gerenciamento de Usuários:**
  - Cadastro de novos usuários (solicitando nome, email e cidade).
  - Login de usuários existentes (baseado no email).
- **Gerenciamento de Eventos:**
  - Cadastro de novos eventos com nome, endereço, categoria, horário (data e hora) e descrição.
  - Utilização de categorias pré-definidas para eventos (Festas, Esportivos, Shows, Conferências, Workshops, Outros).
- **Interação com Eventos:**
  - Consulta de todos os eventos cadastrados, ordenados cronologicamente.
  - Visualização do status do evento (Próximo, Ocorrendo Agora, Já Ocorreu).
  - Confirmação de participação em eventos.
  - Visualização dos eventos em que o usuário confirmou presença.
  - Cancelamento de participação em eventos.
- **Controle Temporal e Notificações:**
  - Ordenação de eventos por data e hora para fácil visualização dos mais próximos.
  - Informação sobre eventos que estão ocorrendo no momento da consulta.
  - Listagem de eventos que já ocorreram.
- **Persistência de Dados:**
  - Salvamento automático das informações dos eventos no arquivo `events.data` na raiz do projeto.
  - Carregamento automático dos eventos a partir do arquivo `events.data` ao iniciar o sistema.

## Tecnologias Utilizadas
- **Linguagem:** Java (OpenJDK/JDK 11 ou superior recomendado)
- **Paradigma:** Programação Orientada a Objetos (POO)
- **Ambiente de Execução:** Console (Terminal)
- **Versionamento:** Git e GitHub

## Pré-requisitos para Execução
- Java Development Kit (JDK) versão 11 ou superior instalado.
- Variável de ambiente `JAVA_HOME` configurada e o diretório `bin` do JDK adicionado ao `PATH` do sistema.

## Estrutura do Projeto
O código fonte está localizado na pasta `src/` e organizado nos seguintes pacotes:
- `br.com.eventos`: Contém o `enum CategoriaEvento.java`.
- `br.com.eventos.app`: Contém a classe principal `SistemaEventosApp.java`.
- `br.com.eventos.modelo`: Contém as classes de dados `Usuario.java` e `Evento.java`.

## Como Compilar e Executar

### 1. Usando um IDE (Ex: VS Code com o Java Extension Pack)
   a. Clone o repositório:
      ```bash
      git clone [https://github.com/ySozun0/projeto-sistema-eventos.git](https://github.com/ySozun0/projeto-sistema-eventos.git)
      ```
   b. Abra a pasta do projeto clonado (`projeto-sistema-eventos`) no seu IDE.
   c. Localize e abra o arquivo `src/br/com/eventos/app/SistemaEventosApp.java`.
   d. Execute o método `main` diretamente através da opção "Run" do seu IDE.

### 2. Manualmente (via Linha de Comando)
   a. Clone o repositório:
      ```bash
      git clone [https://github.com/ySozun0/projeto-sistema-eventos.git](https://github.com/ySozun0/projeto-sistema-eventos.git)
      cd projeto-sistema-eventos
      ```
   b. Compile os arquivos `.java`. A partir da raiz do projeto (`projeto-sistema-eventos/`):
      ```bash
      javac -d bin src/br/com/eventos/CategoriaEvento.java src/br/com/eventos/modelo/Usuario.java src/br/com/eventos/modelo/Evento.java src/br/com/eventos/app/SistemaEventosApp.java
      ```
      *Este comando criará uma pasta `bin` com os arquivos `.class` compilados.*

   c. Execute o programa (a partir da raiz do projeto):
      ```bash
      java -cp bin br.com.eventos.app.SistemaEventosApp
      ```

## Observações
- O arquivo `events.data` será criado (ou lido, se já existir) na pasta raiz do projeto quando o programa for executado.
- O diagrama de classes do projeto pode ser encontrado no arquivo `Diagrama_Aplicativo_de_Eventos_Julio.jpg` neste repositório. *(Certifique-se de que este arquivo de imagem está na raiz do seu projeto e foi adicionado ao Git).*

---
*README elaborado por Júlio C. em 31 de Maio de 2025.*