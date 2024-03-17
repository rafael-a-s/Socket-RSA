package org.sas.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.sas.rsa.GeneratedPairKeysRSA;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChatService {
    
    @Inject
    GeneratedPairKeysRSA generatedPairKeysRSA;

    public void registerUser(String username) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
        generatedPairKeysRSA.generatedKeys(username);
    }

    public String decryptMessage(String message, String username) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, BadPaddingException, InvalidKeySpecException, InvalidKeyException {

        return generatedPairKeysRSA.decrypt(message, username);
    }

    public Map<String, String> convertStringToMap(String data) {
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<Map<String, String>>(){}.getType());
    }
}
