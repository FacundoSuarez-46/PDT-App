package com.fannog.servidor.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import lombok.NoArgsConstructor;
import org.jasypt.util.password.StrongPasswordEncryptor;

@NoArgsConstructor
@LocalBean
@Stateless
public class Encryptor {

    private final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    public String encryptPassword(String password) {
        String result = passwordEncryptor.encryptPassword(password);

        return result;
    }

    public boolean checkPassword(String plainText, String encryptedPassword) {
        return passwordEncryptor.checkPassword(plainText, encryptedPassword);
    }
}
