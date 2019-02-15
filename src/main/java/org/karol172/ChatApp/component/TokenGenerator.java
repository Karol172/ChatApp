package org.karol172.ChatApp.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TokenGenerator {

    private final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUWXYZabcdefghijklmnopqrstuwxyz0123456789";

    private final int TOKEN_LENGTH = 32;

    public TokenGenerator() {
    }

    public String generateToken () {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < TOKEN_LENGTH; i++)
            stringBuffer.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length()-1)));
        return stringBuffer.toString();
    }
}
