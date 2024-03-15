# socket-rsa

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/socket-rsa-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- WebSockets ([guide](https://quarkus.io/guides/websockets)): WebSocket communication channel support

## Provided Code

```java
public void generatedKeys(String username) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPair pair = this.generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        createDirectoryForUser(username);

        savePubKey(username, publicKey);
        savePrivKey(username, privateKey);

        byte[] keyBytes = readKey(username, ContantsProject.PUBLIC_KEY);

        Key publicKey2 = FactoryKey.generatedKey(ContantsProject.PUBLIC_KEY, keyBytes);

        byte[] encryptedMsgBytes = encrypt("Olá Alice!", username, publicKey2);

        String encondedMessage = Base64.getEncoder().encodeToString(encryptedMsgBytes); // Transformando em formato
                                                                                        // legivel
        System.out.println(encondedMessage);

        byte[] privKeyBytes = readKey(username, ContantsProject.PRIVATE_KEY);
        Key privateKey2 = FactoryKey.generatedKey(ContantsProject.PRIVATE_KEY, privKeyBytes);

        byte[] decryptedMsgMessageBytes = decrypt(encryptedMsgBytes, username, privateKey2);
        String decryptedMsg = new String(decryptedMsgMessageBytes, StandardCharsets.UTF_8);

        System.out.println(decryptedMsg);

    }
```
