@usuario
Feature: Gerenciamento de Usuários CMS
  Como um administrador de sistema
  Eu quero gerenciar usuários CMS
  Para que eu possa controlar o acesso ao sistema

  @cadastrar @crud
  Scenario: Criar um novo usuário CMS com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    Then a resposta da API Create deve retornar o código de status 201
    And os dados do usuário na resposta de criação devem estar corretos

  @CT-1001 @api @crud
  Scenario: Realizar login com sucesso
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    Then a resposta da API Login deve retornar o código de status 200
    And o corpo da resposta de login deve conter os dados do usuário

  @CT-1002 @api @crud
  Scenario: Buscar a lista de usuários CMS com autenticação
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    And eu envio a requisição de listar de usuários com autenticação
    Then a resposta da API List deve retornar o código de status 200

  @CT-1003 @api @crud
  Scenario: Busca de usuario por ID
#    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    And eu envio a requisição de busca de usuário por ID
    Then a resposta da API Search deve retornar o código de status 200

#  @CT-1004 @api @crud # DELETE FROM users WHERE nome_completo LIKE 'Usuario Atualizado%';
#  Scenario: Validar alteração de usuário
#    Given que envio uma solicitação 'POST' de registro de usuario CMS
#    When eu realizo o login com as credenciais válidas do usuário criado
#    And que envio a solicitação de PUT com ID
#    Then a resposta da API Update deve retornar o código de status 200
#
#  @LimparEstadoAntes
#  @CT-1005 @api @crud
#  Scenario: Validar exclusao de usuario
#    Given que envio uma solicitação 'POST' de registro de usuario CMS
#    When eu realizo o login com as credenciais válidas do usuário criado
#    And envio uma solicitação de DELETE para o ID
#    Then a resposta da API Delete deve retornar o código de status 204

  @CT-1006 @api
  Scenario: Tentar criar um usuário com e-mail já existente
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When envio novamente uma solicitação 'POST' para registrar o mesmo email
    Then a resposta da API Create deve retornar status 400 com erro no campo "email" e mensagem "E-mail já está em uso"

  @CT-1007 @api
  Scenario: Tentar criar um usuário com nome de usuário já existente
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When envio novamente uma solicitação 'POST' para registrar o mesmo usuário
    Then a resposta da API Create deve retornar status 400 com erro no campo "nomeUsuario" e mensagem "Nome de usuário já está em uso"

  @CT-1008 @api
  Scenario: Tentar criar um usuário com email inválido
    When eu envio uma solicitação de criação de usuário com email inválido
    Then a resposta da API Create deve retornar status 400 com erro no campo "email" e mensagem "Email inválido"

  @CT-1009 @api
  Scenario: Tentar criar um usuário com senha inválida
    When eu envio uma solicitação de criação de usuário com senha inválida
    Then a resposta da API Create deve retornar status 400 com erro no campo "senha" e mensagem "Senha deve ter no mínimo 6 caracteres"

  @CT-1010 @api
  Scenario: Realizar login com dados invalidos
    When eu envio uma solicitação de login com credenciais inválidas
    Then a resposta da API Login deve retornar status 401 com a mensagem de erro "Email ou senha inválidos"

  @CT-1011 @api
  Scenario: Buscar a lista de usuários CMS sem autenticação
    When eu envio a requisição de listar de usuários sem autenticação
    Then a resposta da API List deve retornar status 401 com a mensagem de erro "Token não fornecido"

  @CT-1012 @api
  Scenario: Busca de usuario sem ID
    Given eu envio a requisição de busca de usuário sem ID
    Then a resposta da API Search deve retornar status 401 com a mensagem de erro "Token não fornecido"

  @CT-1013 @api
  Scenario: Validar dados invalidos ou conflitos na alteração de usuário
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When eu realizo o login com as credenciais válidas do usuário criado
    And que envio a solicitação de PUT sem ID
    Then a resposta da API Update deve retornar status 404 com a mensagem de erro "Usuário não encontrado"

  @CT-1014 @api
  Scenario: Validar não é possivel excluir usuario
    Given que envio uma solicitação 'POST' de registro de usuario CMS
    When envio uma solicitação de DELETE para o ID sem autenticação
    Then a resposta da API Delete deve retornar status 401 com a mensagem de erro "Token não fornecido"
