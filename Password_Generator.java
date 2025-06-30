import java.security.SecureRandom;
import java.util.Scanner;

class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:',.<>?/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Password Generator ===");

        System.out.print("Enter password length: ");
        int length = scanner.nextInt();
        scanner.nextLine();  // consume newline

        System.out.print("Include lowercase letters? (y/n): ");
        boolean useLower = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include uppercase letters? (y/n): ");
        boolean useUpper = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include digits? (y/n): ");
        boolean useDigits = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include symbols? (y/n): ");
        boolean useSymbols = scanner.nextLine().trim().equalsIgnoreCase("y");

        if (!useLower && !useUpper && !useDigits && !useSymbols) {
            System.out.println("Error: You must select at least one character set.");
            scanner.close();
            return;
        }

        String password = generatePassword(length, useLower, useUpper, useDigits, useSymbols);
        System.out.println("Generated password: " + password);

        scanner.close();
    }

    private static String generatePassword(int length, boolean useLower, boolean useUpper, boolean useDigits, boolean useSymbols) {
        StringBuilder characterSet = new StringBuilder();

        if (useLower) characterSet.append(LOWERCASE);
        if (useUpper) characterSet.append(UPPERCASE);
        if (useDigits) characterSet.append(DIGITS);
        if (useSymbols) characterSet.append(SYMBOLS);

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterSet.length());
            password.append(characterSet.charAt(index));
        }

        return password.toString();
    }
}
