package org.sas.rsa;

import org.sas.contants.ContantsProject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class GeneratedPairKeysRSA {

    KeyPairGenerator generator;
    private File directory;

    final String PATH_FOLDER = "keys";

    public GeneratedPairKeysRSA() throws NoSuchAlgorithmException {
        this.generator = KeyPairGenerator.getInstance("RSA");
        this.generator.initialize(1024);
    }

    public void generatedKeys(String username) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPair pair = this.generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();


        String pubFileName = username + ContantsProject.SUFIX_PUB_FILE;

        File file = new File(this.getDirectory(),pubFileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File publicFile = new File(this.getDirectory() ,pubFileName);
        byte[] publicFileBytes = Files.readAllBytes(publicFile.toPath());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicFileBytes);
        keyFactory.generatePublic(pubKeySpec);

        String msg = "Oi alice";

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] secretMsgBytes = msg.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMsgBytes = encryptCipher.doFinal(secretMsgBytes);

        String encondedMessage = Base64.getEncoder().encodeToString(encryptedMsgBytes);
        System.out.println(encondedMessage);

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedMsgMessageBytes = decryptCipher.doFinal(encryptedMsgBytes);
        String decryptedMsg = new String(decryptedMsgMessageBytes, StandardCharsets.UTF_8);

        System.out.println(decryptedMsg);

    }

    public File getDirectory() {
        if(directory == null) {
            this.directory = new File(PATH_FOLDER);
            if (!this.directory.exists()) {
                this.directory.mkdirs();
            }
        }

        return directory;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        GeneratedPairKeysRSA grsa = new GeneratedPairKeysRSA();

        grsa.generatedKeys("bob");
    }
}
