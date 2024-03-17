package org.sas.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import org.sas.contants.ContantsProject;
import org.sas.rsa.GeneratedPairKeysRSA;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyService {
    
    @Inject
    GeneratedPairKeysRSA generatedPairKeysRSA;

    public String getKeyPub(String username) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return generatedPairKeysRSA.readPunPem(username);
    }
}
