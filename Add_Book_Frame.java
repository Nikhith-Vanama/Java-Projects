import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AddBookFrame extends JFrame {
    private JTextField titleField, authorField, quantityField;
    private JButton addButton;

    public AddBookFrame() {
        setTitle("Add Book");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        titleField = new JTextField(15);
        authorField = new JTextField(15);
        quantityField = new JTextField(5);
        addButton = new JButton("Add Book");

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel());
        panel.add(addButton);

        add(panel);

        addButton.addActionListener(e -> addBook());
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Book added successfully.");
                titleField.setText("");
                authorField.setText("");
                quantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        }
    }
}
