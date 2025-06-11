import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

class EcommerceCartSystem extends JFrame {
    private JTable productTable, cartTable;
    private DefaultTableModel productModel, cartModel;
    private JButton addToCartBtn, checkoutBtn;
    private ArrayList<CartItem> cartItems = new ArrayList<>();

    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Nick@2403";

    public EcommerceCartSystem() {
        setTitle("E-commerce Cart System");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Products Panel
        productModel = new DefaultTableModel(new String[]{"ID", "Name", "Price"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        productTable = new JTable(productModel);
        JScrollPane productScroll = new JScrollPane(productTable);

        // Cart Panel
        cartModel = new DefaultTableModel(new String[]{"Name", "Price", "Quantity"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        cartTable = new JTable(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);

        // Buttons
        addToCartBtn = new JButton("Add to Cart");
        checkoutBtn = new JButton("Checkout");

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(productScroll);
        centerPanel.add(cartScroll);

        JPanel southPanel = new JPanel();
        southPanel.add(addToCartBtn);
        southPanel.add(checkoutBtn);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        loadProducts();

        addToCartBtn.addActionListener(e -> addToCart());
        checkoutBtn.addActionListener(e -> checkout());
    }

    private void loadProducts() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            productModel.setRowCount(0);
            while (rs.next()) {
                productModel.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getDouble("price")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }
        int id = (int) productModel.getValueAt(selectedRow, 0);
        String name = (String) productModel.getValueAt(selectedRow, 1);
        double price = (double) productModel.getValueAt(selectedRow, 2);

        for (CartItem item : cartItems) {
            if (item.productId == id) {
                item.quantity++;
                refreshCartTable();
                return;
            }
        }
        cartItems.add(new CartItem(id, name, price, 1));
        refreshCartTable();
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        for (CartItem item : cartItems) {
            cartModel.addRow(new Object[]{item.name, item.price, item.quantity});
        }
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }
        double total = 0;
        StringBuilder receipt = new StringBuilder("Receipt:\n");
        for (CartItem item : cartItems) {
            double subTotal = item.price * item.quantity;
            receipt.append(String.format("%s x%d = $%.2f\n", item.name, item.quantity, subTotal));
            total += subTotal;
        }
        receipt.append(String.format("Total: $%.2f", total));

        JOptionPane.showMessageDialog(this, receipt.toString());
        cartItems.clear();
        refreshCartTable();
    }

    static class CartItem {
        int productId;
        String name;
        double price;
        int quantity;

        CartItem(int productId, String name, double price, int quantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EcommerceCartSystem().setVisible(true);
        });
    }
}
