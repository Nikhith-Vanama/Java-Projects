import java.sql.*;
import java.util.Scanner;

class ATMInterface {
    static final String URL = "jdbc:mysql://localhost:3306/atm_db";
    static final String USER = "root";
    static final String PASS = "Nick@2403"; // <-- Set your MySQL password here

    static Connection conn;
    static Scanner sc = new Scanner(System.in);
    static int accNo = 0;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);

            if (login()) {
                int choice;
                do {
                    System.out.println("\n=== ATM Menu ===");
                    System.out.println("1. Check Balance");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. Mini Statement");
                    System.out.println("5. Exit");
                    System.out.print("Choose: ");
                    choice = sc.nextInt();

                    switch (choice) {
                        case 1 -> checkBalance();
                        case 2 -> deposit();
                        case 3 -> withdraw();
                        case 4 -> miniStatement();
                        case 5 -> System.out.println("Thank you for using the ATM.");
                        default -> System.out.println("Invalid option.");
                    }
                } while (choice != 5);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean login() throws SQLException {
        System.out.print("Enter Account No: ");
        accNo = sc.nextInt();
        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        String sql = "SELECT * FROM accounts WHERE acc_no=? AND pin=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accNo);
        ps.setInt(2, pin);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Welcome, " + rs.getString("name"));
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }

    static void checkBalance() throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE acc_no=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accNo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Current Balance: ₹" + rs.getDouble("balance"));
        }
    }

    static void deposit() throws SQLException {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();

        String update = "UPDATE accounts SET balance = balance + ? WHERE acc_no=?";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.setDouble(1, amount);
        ps.setInt(2, accNo);
        ps.executeUpdate();

        recordTransaction("Deposit", amount);
        System.out.println("Amount deposited successfully.");
    }

    static void withdraw() throws SQLException {
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        String check = "SELECT balance FROM accounts WHERE acc_no=?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setInt(1, accNo);
        ResultSet rs = ps1.executeQuery();

        if (rs.next() && rs.getDouble("balance") >= amount) {
            String update = "UPDATE accounts SET balance = balance - ? WHERE acc_no=?";
            PreparedStatement ps2 = conn.prepareStatement(update);
            ps2.setDouble(1, amount);
            ps2.setInt(2, accNo);
            ps2.executeUpdate();

            recordTransaction("Withdraw", amount);
            System.out.println("Please collect your cash.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    static void miniStatement() throws SQLException {
        System.out.println("\n--- Mini Statement ---");
        String sql = "SELECT * FROM transactions WHERE acc_no=? ORDER BY timestamp DESC LIMIT 5";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accNo);
        ResultSet rs = ps.executeQuery();

        System.out.printf("%-10s %-10s %-10s\n", "Type", "Amount", "Date");
        while (rs.next()) {
            System.out.printf("%-10s ₹%-10.2f %s\n",
                rs.getString("type"),
                rs.getDouble("amount"),
                rs.getTimestamp("timestamp"));
        }
    }

    static void recordTransaction(String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (acc_no, type, amount) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, accNo);
        ps.setString(2, type);
        ps.setDouble(3, amount);
        ps.executeUpdate();
    }
}
