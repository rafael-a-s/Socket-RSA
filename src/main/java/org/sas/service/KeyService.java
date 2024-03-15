package org.sas.service;

import java.io.IOException;
import java.util.Base64;

import org.sas.contants.ContantsProject;
import org.sas.rsa.GeneratedPairKeysRSA;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class KeyService {
    
    @Inject
    GeneratedPairKeysRSA generatedPairKeysRSA;

    public String getKeyPub(String username) throws IOException {
        var pubKeyByte = generatedPairKeysRSA.readKey(username, ContantsProject.PUBLIC_KEY);
        return new String(pubKeyByte);
    }
}
