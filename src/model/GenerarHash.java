package model;

import org.mindrot.jbcrypt.BCrypt;

public class GenerarHash {

    public static void main(String[] args) {

        String password = "123456";

        String hash = BCrypt.hashpw(
            password,
            BCrypt.gensalt()
        );

        System.out.println("Hash generado: " + hash);
    }
}