import java.sql.*;
import java.util.Scanner;

class StudentResultSystem {
    static final String URL = "jdbc:mysql://localhost:3306/student_db";
    static final String USER = "root";
    static final String PASSWORD = "Nick@2403";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
                System.out.println("\n--- Student Result Management ---");
                System.out.println("1. Add Result");
                System.out.println("2. View Results");
                System.out.println("3. Edit Result");
                System.out.println("4. Delete Result");
                System.out.println("5. Print Report Card");
                System.out.println("6. Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();
                sc.nextLine(); // clear buffer

                switch (choice) {
                    case 1 -> addResult(con, sc);
                    case 2 -> viewResults(con);
                    case 3 -> editResult(con, sc);
                    case 4 -> deleteResult(con, sc);
                    case 5 -> printReportCard(con, sc);
                    case 6 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addResult(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter student name: ");
        String name = sc.nextLine();
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine();
        System.out.print("Enter subject: ");
        String subject = sc.nextLine();
        System.out.print("Enter marks: ");
        int marks = sc.nextInt();

        String sql = "INSERT INTO results (student_name, roll_no, subject, marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, roll);
            ps.setString(3, subject);
            ps.setInt(4, marks);
            ps.executeUpdate();
            System.out.println("Result added.");
        }
    }

    static void viewResults(Connection con) throws SQLException {
        String sql = "SELECT * FROM results";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("student_name") +
                        ", Roll: " + rs.getString("roll_no") +
                        ", Subject: " + rs.getString("subject") +
                        ", Marks: " + rs.getInt("marks"));
            }
        }
    }

    static void editResult(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter result ID to edit: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new marks: ");
        int newMarks = sc.nextInt();

        String sql = "UPDATE results SET marks=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, newMarks);
            ps.setInt(2, id);
            int updated = ps.executeUpdate();
            System.out.println(updated > 0 ? "Updated successfully." : "Result not found.");
        }
    }

    static void deleteResult(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter result ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM results WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int deleted = ps.executeUpdate();
            System.out.println(deleted > 0 ? "Deleted successfully." : "Result not found.");
        }
    }

    static void printReportCard(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine();

        String sql = "SELECT subject, marks FROM results WHERE roll_no=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, roll);
            ResultSet rs = ps.executeQuery();
            System.out.println("--- Report Card ---");
            int total = 0;
            int count = 0;
            while (rs.next()) {
                System.out.println(rs.getString("subject") + ": " + rs.getInt("marks"));
                total += rs.getInt("marks");
                count++;
            }
            if (count > 0) {
                System.out.println("Total: " + total + ", Average: " + (total / count));
            } else {
                System.out.println("No records found.");
            }
        }
    }
}
