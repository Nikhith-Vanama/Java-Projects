import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

class StudentPanel extends JFrame {
    int studentId;
    JTextField bookIdField;
    JButton issueBook, returnBook;

    public StudentPanel(int studentId) {
        this.studentId = studentId;

        setTitle("Student Panel");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bookIdField = new JTextField(10);
        issueBook = new JButton("Issue Book");
        returnBook = new JButton("Return Book");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(new JLabel("Book ID:"));
        panel.add(bookIdField);
        panel.add(issueBook);
        panel.add(returnBook);

        add(panel);

        issueBook.addActionListener(e -> issueBook());
        returnBook.addActionListener(e -> returnBook());
    }

private void issueBook() {
    String bookIdText = bookIdField.getText().trim();

    if (bookIdText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a Book ID.");
        return;
    }

    int bookId;
    try {
        bookId = Integer.parseInt(bookIdText);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid Book ID. Please enter a numeric value.");
        return;
    }

    // Now safely use bookId
    try (Connection con = DBConnection.getConnection()) {
        // Example logic: check if book exists, is available, and then issue
        PreparedStatement ps = con.prepareStatement("SELECT * FROM books WHERE id = ?");
        ps.setInt(1, bookId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Issue logic here
            PreparedStatement issue = con.prepareStatement(
                "INSERT INTO issued_books (student_id, book_id, issue_date) VALUES (?, ?, NOW())");
            issue.setInt(1, studentId);
            issue.setInt(2, bookId);
            issue.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book issued successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error.");
    }
}


    private void returnBook() {
        try (Connection con = DBConnection.getConnection()) {
            int bookId = Integer.parseInt(bookIdField.getText());
            PreparedStatement ps = con.prepareStatement("UPDATE issued_books SET return_date=? WHERE book_id=? AND student_id=? AND return_date IS NULL");
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, bookId);
            ps.setInt(3, studentId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                PreparedStatement updateBook = con.prepareStatement("UPDATE books SET available=true WHERE book_id=?");
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();
                JOptionPane.showMessageDialog(this, "Book Returned");
            } else {
                JOptionPane.showMessageDialog(this, "No record found or already returned");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
