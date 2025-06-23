import java.util.Scanner;
import java.util.Random;

class NumberGuessingGame {
    public static void main(String[] args) {
        final int MAX_ATTEMPTS = 5;
        int attempts = 0;
        boolean hasWon = false;

        Random rand = new Random();
        int numberToGuess = rand.nextInt(100) + 1; // 1 to 100

        Scanner scanner = new Scanner(System.in);

        System.out.println("🎯 Welcome to the Number Guessing Game!");
        System.out.println("I have picked a number between 1 and 100.");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess it.\n");

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Attempt " + (attempts + 1) + ": Enter your guess: ");
            int userGuess;

            // Input validation
            if (!scanner.hasNextInt()) {
                System.out.println("❌ Please enter a valid number.");
                scanner.next(); // consume invalid input
                continue;
            }

            userGuess = scanner.nextInt();
            attempts++;

            if (userGuess == numberToGuess) {
                hasWon = true;
                break;
            } else if (userGuess < numberToGuess) {
                System.out.println("📉 Too low!");
            } else {
                System.out.println("📈 Too high!");
            }
        }

        if (hasWon) {
            System.out.println("\n🎉 Congratulations! You guessed the number in " + attempts + " attempts.");
        } else {
            System.out.println("\n😞 Sorry, you've used all your attempts.");
            System.out.println("The correct number was: " + numberToGuess);
        }

        scanner.close();
    }
}
