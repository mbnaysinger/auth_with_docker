package br.com.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Senha original: " + rawPassword);
        System.out.println("Senha encodada para inserir no banco: " + encodedPassword);
    }
}
