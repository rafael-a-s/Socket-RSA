package org.sas.rsa;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.sas.contants.ContantsProject;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ApplicationScoped
public class GeneratedPairKeysRSA {

    KeyPairGenerator generator;
    private File directory;

    final String PATH_FOLDER = "keys";

    public GeneratedPairKeysRSA() throws NoSuchAlgorithmException {
        this.generator = KeyPairGenerator.getInstance("RSA");
        this.generator.initialize(1024);
    }

    public void generatedKeys(String username) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyPair pair = this.generator.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        createDirectoryForUser(username);

        savePubKey(username, publicKey);
        savePrivKey(username, privateKey);
    }

    byte[] encrypt(String msg, String username, Key key) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

        return cipher.doFinal(msgByte);
    }

    public String decrypt(String encryptedMsg, String username)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        Key privateKey = readKey(username, ContantsProject.PRIVATE_KEY);

        byte[] encryptedMsgBytes = Base64.getDecoder().decode(encryptedMsg);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(encryptedMsgBytes), StandardCharsets.UTF_8);
    }

    public File getDirectory() {
        if (directory == null) {
            this.directory = new File(PATH_FOLDER);
            if (!this.directory.exists()) {
                this.directory.mkdirs();
            }
        }

        return directory;
    }

    boolean createDirectoryForUser(String username) {
        File folder = new File(getDirectory(), username);
        if (!folder.exists()) {
            folder.mkdirs();
            return folder.exists();
        }

        return false;
    }

    void savePubKey(String username, PublicKey publicKey) {
        File file = new File(this.getDirectory() + "/" + username, username + ContantsProject.SUFIX_PUB_FILE.getName());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void savePrivKey(String username, PrivateKey privateKey) {
        File file = new File(this.getDirectory() + "/" + username,
                username + ContantsProject.SUFIX_PRIV_FILE.getName());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(privateKey.getEncoded());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Key readKey(String username, ContantsProject key) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        if (key.equals(ContantsProject.PRIVATE_KEY)) {
            File privateFile = new File(this.getDirectory() + "/" + username,
                    username + ContantsProject.SUFIX_PRIV_FILE.getName());
            return FactoryKey.privateKey(Files.readAllBytes(privateFile.toPath()));
        }
        File publicFile = new File(this.getDirectory() + "/" + username,
                username + ContantsProject.SUFIX_PUB_FILE.getName());
        return FactoryKey.publicKey(Files.readAllBytes(publicFile.toPath()));
    }

    public String readPunPem(String username) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File publicFile = new File(this.getDirectory() + "/" + username,
                username + ContantsProject.SUFIX_PUB_FILE.getName());
        var fileBytes = Files.readAllBytes(publicFile.toPath());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(fileBytes);
        PublicKey key = keyFactory.generatePublic(pubKeySpec);

        StringWriter stringWriter = new StringWriter();

        try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
            pemWriter.writeObject(key);
        }

        return stringWriter.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        GeneratedPairKeysRSA grsa = new GeneratedPairKeysRSA();

        grsa.generatedKeys("bob");
    }
}
