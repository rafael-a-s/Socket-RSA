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


    createDirectoryForUser(username);

       savePubKey(username, publicKey);
       savePrivKey(username, privateKey);

        byte[] keyBytes = readKey(username, ContantsProject.PUBLIC_KEY);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
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

    boolean createDirectoryForUser(String username) {
        File folder = new File(getDirectory(),username);
        if (!folder.exists()) {
            folder.mkdirs();
            return folder.exists();
        }

        return false;
    }

    void savePubKey(String username, PublicKey publicKey ) {
        File file = new File(this.getDirectory() +"/"+ username, username + ContantsProject.SUFIX_PUB_FILE.getName());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void savePrivKey(String username, PrivateKey privateKey ) {
        File file = new File(this.getDirectory() + "/" + username, username + ContantsProject.SUFIX_PRIV_FILE.getName());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(privateKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    byte[] readKey(String username, ContantsProject key) throws IOException{
        if(key.equals(ContantsProject.PRIVATE_KEY)) {
            File privateFile = new File(this.getDirectory()+ "/" + username , username + ContantsProject.SUFIX_PRIV_FILE.getName());
            return Files.readAllBytes(privateFile.toPath());
        }
        File publicFile = new File(this.getDirectory() + "/" + username , username + ContantsProject.SUFIX_PUB_FILE.getName());
        return Files.readAllBytes(publicFile.toPath());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        GeneratedPairKeysRSA grsa = new GeneratedPairKeysRSA();

        grsa.generatedKeys("alice");
    }
}
