import java.sql.*;
import java.util.Scanner;

class InventoryManager {
    static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    static final String USER = "root";        // Change if needed
    static final String PASS = "Nick@2403";    // Change if needed

    static Connection conn;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Scanner sc = new Scanner(System.in);

            int choice;
            do {
                System.out.println("\n--- Inventory Management ---");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1: addProduct(sc); break;
                    case 2: viewProducts(); break;
                    case 3: updateProduct(sc); break;
                    case 4: deleteProduct(sc); break;
                    case 5: System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid choice.");
                }
            } while (choice != 5);

            conn.close();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addProduct(Scanner sc) throws SQLException {
        System.out.print("Enter name: ");
        sc.nextLine();
        String name = sc.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();
        System.out.print("Enter price: ");
        double price = sc.nextDouble();

        String sql = "INSERT INTO products (name, quantity, price) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, quantity);
        ps.setDouble(3, price);
        ps.executeUpdate();
        System.out.println("Product added.");
    }

    static void viewProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\nID\tName\t\tQty\tPrice");
        while (rs.next()) {
            System.out.printf("%d\t%s\t\t%d\t%.2f\n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"));
        }
    }

    static void updateProduct(Scanner sc) throws SQLException {
        System.out.print("Enter product ID to update: ");
        int id = sc.nextInt();

        System.out.print("Enter new quantity: ");
        int quantity = sc.nextInt();
        System.out.print("Enter new price: ");
        double price = sc.nextDouble();

        String sql = "UPDATE products SET quantity=?, price=? WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, quantity);
        ps.setDouble(2, price);
        ps.setInt(3, id);
        int rows = ps.executeUpdate();

        if (rows > 0) System.out.println("Product updated.");
        else System.out.println("Product not found.");
    }

    static void deleteProduct(Scanner sc) throws SQLException {
        System.out.print("Enter product ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM products WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int rows = ps.executeUpdate();

        if (rows > 0) System.out.println("Product deleted.");
        else System.out.println("Product not found.");
    }
}
