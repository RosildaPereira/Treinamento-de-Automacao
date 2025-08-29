@login
  Feature: Validação de login
    Como um usuário
  Eu quero validar o login
    Para acessar a aplicação

    @CT-0001
    Scenario: Realizar login com usuário CMS
      Given eu envio uma solicitação 'GET' de login com as 'credenciais do usuário'
      Then a resposta da API retorna o status code 200 para 'login de usuário'
      And o token de autenticação deve ser retornado