package org.sas.rsa;
import org.sas.contants.ContantsProject;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class FactoryKey {

    public static Key generatedKey(ContantsProject key, byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(key.equals(ContantsProject.PUBLIC_KEY)){
            return publicKey(keyBytes);
        } else if(key.equals(ContantsProject.PRIVATE_KEY)){
            return privateKey(keyBytes);
        } else {
            throw new IllegalArgumentException("Tipo solicitado inválido.");
        }
    }

    static PublicKey publicKey(byte[] pubByte) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubByte);
        return keyFactory.generatePublic(pubKeySpec);
    }

    static PrivateKey privateKey(byte[] privKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKey);
        return keyFactory.generatePrivate(privKeySpec);
    }
    
}
