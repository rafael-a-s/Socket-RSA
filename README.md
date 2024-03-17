# Projeto Chat Quarkus com Criptografia RSA

Este projeto é uma aplicação de chat que utiliza a tecnologia Quarkus para o backend e WebSockets para comunicação em tempo real. A segurança das mensagens é assegurada por meio da criptografia RSA, garantindo que as mensagens sejam enviadas de forma segura entre os usuários.

## Funcionalidades

- **Conexão WebSocket**: Os usuários se conectam ao chat por meio de uma conexão WebSocket, que permite a comunicação bidirecional em tempo real.
- **Criptografia RSA**: As mensagens são criptografadas no lado do cliente usando a chave pública do destinatário e descriptografadas no lado do servidor usando a chave privada correspondente.
- **Geração de Chaves RSA**: No registro de um novo usuário, um par de chaves RSA (pública e privada) é gerado para esse usuário.

## Endpoints

### `GET /key/{username}`

- **Descrição**: Este endpoint é responsável por fornecer a chave pública do usuário especificado pelo `{username}`. Essa chave pública é usada para criptografar as mensagens antes de serem enviadas.
- **Parâmetros**: `username` - O nome de usuário cuja chave pública é solicitada.
- **Retorno**: A chave pública no formato PEM.

## WebSocket `/chat/{username}`

- **Descrição**: Endpoint WebSocket para a conexão do chat. O `{username}` é o nome do usuário que está se conectando ao chat.
- **Eventos**:
    - **OnOpen**: Disparado quando um usuário se conecta ao WebSocket.
    - **OnClose**: Disparado quando um usuário se desconecta do WebSocket.
    - **OnError**: Disparado quando ocorre um erro na conexão WebSocket.
    - **OnMessage**: Disparado quando uma mensagem é recebida pelo WebSocket.

## Criptografia e Descriptografia

### Criptografia (Client-Side)

As mensagens são criptografadas no lado do cliente usando a chave pública do destinatário. A biblioteca `forge` é utilizada para essa finalidade.

```javascript
function encryptMessage(message, publicKey) {
    var forgePublicKey = forge.pki.publicKeyFromPem(publicKey);
    var bytes = forge.util.encodeUtf8(message);
    var encrypted = forgePublicKey.encrypt(bytes, 'RSA-OAEP');
    var base64 = forge.util.encode64(encrypted);
    return base64;
}
```

### Descriptografia (Server-Side)

As mensagens criptografadas recebidas pelo servidor são descriptografadas usando a chave privada do destinatário.

```java
public String decrypt(String encryptedMsg, String username) throws ... {
    Key privateKey = readKey(username, ContantsProject.PRIVATE_KEY);
    byte[] encryptedMsgBytes = Base64.getDecoder().decode(encryptedMsg);
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return new String(cipher.doFinal(encryptedMsgBytes), StandardCharsets.UTF_8);
}
```

## Instruções de Uso

- **Conectar:** Ao entrar no chat, o usuário insere seu nome de usuário e clica em "Conectar". Isso estabelece uma conexão WebSocket com o servidor e registra o usuário no chat.
- **Conversa com um amigo:** O usuário insere o nome do amigo com quem deseja conversar e clica em "Conectar com seu amigo". O sistema então busca a chave pública desse amigo para criptografar as mensagens.
- **Enviar Mensagem:** O usuário digita sua mensagem e clica em "Enviar". A mensagem é criptografada usando a chave pública do destinatário e enviada ao servidor por meio da conexão WebSocket.
- **Receber Mensagens:** As mensagens criptografadas recebidas são descriptografadas no servidor usando a chave privada do destinatário e transmitidas para o destinatário por meio da conexão WebSocket.

## Execução do Projeto

ara executar este projeto, você precisará ter o JDK 17 instalado em sua máquina. Você pode verificar a versão do seu JDK usando o comando:

```bash
java -version
```

Se o JDK 17 não estiver instalado, você pode baixá-lo e instalá-lo a partir do site oficial do Oracle JDK ou usar uma versão do OpenJDK disponível em AdoptOpenJDK.

### Usando a Linha de Comando

Para executar o projeto a partir da linha de comando, navegue até o diretório raiz do projeto e execute o seguinte comando:
```bash
./gradlew quarkusDev
```
Este comando iniciará o modo de desenvolvimento do Quarkus, que permite a compilação e recarga ao vivo do código. Você verá a saída no console indicando que o servidor está sendo executado e em qual porta ele está disponível (geralmente é a porta 8080).

### Acessando o chat

Depois que o servidor estiver em execução, você poderá acessar o chat abrindo um navegador e indo para http://localhost:8080. Se você usou uma porta diferente, ajuste a URL de acordo.

## Considerações

Este projeto é um exemplo de implementação e não deve ser usado como está em ambientes de produção sem revisões de segurança adequadas, especialmente em relação ao gerenciamento e armazenamento de chaves privadas.