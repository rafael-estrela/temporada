# App Temporada

Projeto realizado para avaliação de técnicas de desenvolvimento Android.

### A tarefa

Criar um aplicativo que utilize a API pública [TheMovieDB](https://www.themoviedb.org/).

Esse aplicativo deverá listar as séries de TV populares em uma tela com rolagem infinita e apresentar os detalhes da série em uma tela auxiliar. A lista deve exibir **título**, **média de votos**, **poster** e **ano de primeira aparição**. Os detalhes devem apresentar **título**, **média de votos**, **poster**, **ano de primeira aparição**, **resumo**, **gêneros** e **listagem de shows similares**.

### Sobre o projeto

*  Técnica de desenvolvimento: _Test Driven Development_ (**TDD**);
*  Linguagem: **Kotlin 1.3.72**;
*  IDE: **AndroidStudio 3.6.3**;
*  **Gradle 3.6.3**, _dist_ **5.6.4**;
*  Demais informações técnicas:
    *  Projeto utilizando **AndroidX**;
    *  Implementação de **Menu** e **SearchView**;
    *  _Layouts_ utilizam **RecyclerView**, **NestedScrollView** e **ConstraintLayout**;
    *  Análise de código com **Ktlint 0.36.0**;
    *  Utilização de **DataBinding**, **Lifecycle 2.2.0** e **ReactiveExtensions**:
        *  **RxAndroid 2.1.1**;
        *  **RxJava 2.2.10**;
        *  **RxKotlin 2.4.0**.
    *  Dependências para API _digest_:
        *  **Retrofit 2.7.1**;
        *  **Gson Converter 2.7.1**;
        *  **RxJava2 Adapter 2.7.1**;
        *  **OkHttp LoggingInterceptor 4.4.0**;
        *  **Picasso 2.5.2**.
    *  Dependências para testes unitários:
        *  **JUnit 4.12**;
        *  **Mockito 3.1.0**;
        *  **MockitoKotlin 2.2.0**;
        *  **MockWebServer 4.4.0**;
        *  **Robolectric 4.1**.

### Características da aplicação
* Tela inicial: lista de séries mais populares (retorno padrão da API), apresentada em uma _grid_ com 2 ou 3 colunas, de acordo com a orientação do dispositivo. São exibidos 20 séries por página.
* Tela de detalhes: detalhes da série, assim como uma lista com rolagem infinita das séries similares, exibidas em uma lista horizontal de rolagem infinita. É possível interagir com os itens dessa lista e visualizar seus detalhes.
* Caso alguma requisição falhe, é possível solicitar sua repetição.
* Cenários que envolvem mais de uma requisição para carregar o conteúdo da tela foram otimizados com chamadas paralelas.
* Layouts validados e adaptados em dispositivos de tamanho regular e dispositivos grandes (_tablets_), nas duas orientações de tela.

### Blueprint

O projeto foi desenvolvido considerando as seguintes camadas:

* _Model_: define a estrutura dos dados que transitam no app e o contrato que deve ser aguardado da API.
* _Network_: realiza chamadas de API para o backend. Nesta camada ficam as configurações da API e os _Services_.
* _Navigation_: realiza o fluxo de telas no app. Nesta camada ficam os _Routers_.
* _Business_: trata das regras de negócio do projeto. Nesta camada ficam as _ViewModels_.
* _View_: apresenta as informações para o usuário. Nesta camada ficam as _Activities_, _Adapters_ e _ViewHolders_.

### Testes Unitários

Foram realizados testes unitários nas camadas de _Network_, _Navigation_ e _Business_.

A cobertura geral dos testes ficou da seguinte maneira:

![Cobertura geral](documentation/coverage_general.png)

A cobertura por camada ficou da seguinte maneira:

* _Network_:

![Cobertura camada Network - Config](documentation/coverage_network_config.png)

![Cobertura camada Network - Services](documentation/coverage_network_service.png)

* _Navigation_:

![Cobertura camada Navigation](documentation/coverage_navigation.png)

* _Business_:

![Cobertura camada Business](documentation/coverage_business.png)
