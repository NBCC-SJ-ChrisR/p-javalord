package DataAccess;

import org.mindrot.jbcrypt.BCrypt;

public class Crypt {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static void main(String[] args) {
        String password = "password";
        String hashedPassword = hashPassword(password);
        System.out.println("Passwords" + hashedPassword);
        boolean checkPassword = checkPassword(password, hashedPassword);
        System.out.println("Pass Match?" + checkPassword);

    }
}
