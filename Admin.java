import java.sql.*;
import java.util.Scanner;

class Admin {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter question:");
            String q = sc.nextLine();
            System.out.println("Option 1:");
            String o1 = sc.nextLine();
            System.out.println("Option 2:");
            String o2 = sc.nextLine();
            System.out.println("Option 3:");
            String o3 = sc.nextLine();
            System.out.println("Option 4:");
            String o4 = sc.nextLine();
            System.out.println("Correct option (1-4):");
            int correct = sc.nextInt();

            String sql = "INSERT INTO questions (question, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, q);
            ps.setString(2, o1);
            ps.setString(3, o2);
            ps.setString(4, o3);
            ps.setString(5, o4);
            ps.setInt(6, correct);
            ps.executeUpdate();
            System.out.println("Question added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
