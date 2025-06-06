import java.io.*;
import java.util.*;

class BankAccount {
    String accountNumber;
    String holderName;
    double balance;

    public BankAccount(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("✅ Deposited: " + amount);
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("❌ Insufficient balance!");
        } else {
            balance -= amount;
            System.out.println("✅ Withdrawn: " + amount);
        }
    }

    public void checkBalance() {
        System.out.println("💰 Current Balance: " + balance);
    }

    public String toFileString() {
        return accountNumber + "," + holderName + "," + balance;
    }

    public static BankAccount fromFileString(String line) {
        String[] parts = line.split(",");
        return new BankAccount(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

class BankManagementSystem {
    static final String FILE_NAME = "accounts.txt";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🏦 Welcome to Bank Management System");
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> performTransaction("deposit");
                case 3 -> performTransaction("withdraw");
                case 4 -> performTransaction("check");
                case 5 -> {
                    System.out.println("👋 Thank you for using the system.");
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }

    static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();

        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Deposit: ");
        double amount = scanner.nextDouble();

        BankAccount account = new BankAccount(accNo, name, amount);
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(account.toFileString() + "\n");
            System.out.println("✅ Account created successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error saving account.");
        }
    }

    static void performTransaction(String type) {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();

        List<BankAccount> accounts = loadAccounts();
        boolean found = false;

        for (BankAccount acc : accounts) {
            if (acc.accountNumber.equals(accNo)) {
                found = true;
                switch (type) {
                    case "deposit" -> {
                        System.out.print("Enter amount to deposit: ");
                        double amt = scanner.nextDouble();
                        acc.deposit(amt);
                    }
                    case "withdraw" -> {
                        System.out.print("Enter amount to withdraw: ");
                        double amt = scanner.nextDouble();
                        acc.withdraw(amt);
                    }
                    case "check" -> acc.checkBalance();
                }
                break;
            }
        }

        if (!found) {
            System.out.println("❌ Account not found!");
        } else {
            saveAccounts(accounts);
        }
    }

    static List<BankAccount> loadAccounts() {
        List<BankAccount> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(BankAccount.fromFileString(line));
            }
        } catch (IOException e) {
            // No accounts yet
        }
        return list;
    }

    static void saveAccounts(List<BankAccount> accounts) {
        try (FileWriter fw = new FileWriter(FILE_NAME, false)) {
            for (BankAccount acc : accounts) {
                fw.write(acc.toFileString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("❌ Error updating account.");
        }
    }
}
