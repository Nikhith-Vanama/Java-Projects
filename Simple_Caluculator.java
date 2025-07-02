import java.util.Scanner;

class SimpleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double num1, num2, result;
        char operator;

        System.out.println("🔢 Simple Calculator");
        System.out.print("Enter first number: ");
        num1 = scanner.nextDouble();

        System.out.print("Enter an operator (+, -, *, /): ");
        operator = scanner.next().charAt(0);

        System.out.print("Enter second number: ");
        num2 = scanner.nextDouble();

        switch (operator) {
            case '+':
                result = num1 + num2;
                System.out.println("✅ Result: " + result);
                break;

            case '-':
                result = num1 - num2;
                System.out.println("✅ Result: " + result);
                break;

            case '*':
                result = num1 * num2;
                System.out.println("✅ Result: " + result);
                break;

            case '/':
                if (num2 == 0) {
                    System.out.println("❌ Cannot divide by zero.");
                } else {
                    result = num1 / num2;
                    System.out.println("✅ Result: " + result);
                }
                break;

            default:
                System.out.println("❌ Invalid operator.");
        }

        scanner.close();
    }
}
