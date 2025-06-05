import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class AdminPanel extends JFrame {
    JTextField titleField, authorField;
    JButton addBook;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        titleField = new JTextField(15);
        authorField = new JTextField(15);
        addBook = new JButton("Add Book");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel());
        panel.add(addBook);

        add(panel);

        addBook.addActionListener(e -> addBook());
    }

    private void addBook() {
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO books (title, author) VALUES (?, ?)");
            ps.setString(1, titleField.getText());
            ps.setString(2, authorField.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
