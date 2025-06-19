import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class HotelBookingSystem extends JFrame {
    Connection conn;
    JTextField nameField, roomField, inField, outField;

    public HotelBookingSystem() {
        setTitle("Hotel Booking System");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2));

        // GUI Components
        add(new JLabel("Guest Name:"));
        nameField = new JTextField(); add(nameField);

        add(new JLabel("Room No:"));
        roomField = new JTextField(); add(roomField);

        add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        inField = new JTextField(); add(inField);

        add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        outField = new JTextField(); add(outField);

        JButton bookBtn = new JButton("Book Room");
        JButton checkinBtn = new JButton("Check-in");
        JButton checkoutBtn = new JButton("Check-out");

        add(bookBtn);
        add(checkinBtn);
        add(checkoutBtn);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotel_db", "root", "Nick@2403"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }

        bookBtn.addActionListener(e -> bookRoom());
        checkinBtn.addActionListener(e -> checkInRoom());
        checkoutBtn.addActionListener(e -> checkOutRoom());

        setVisible(true);
    }

void bookRoom() {
    String guest = nameField.getText().trim();
    String roomStr = roomField.getText().trim();
    String checkIn = inField.getText().trim();
    String checkOut = outField.getText().trim();

    if (guest.isEmpty() || roomStr.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.");
        return;
    }

    try {
        int room = Integer.parseInt(roomStr);

        // Check if room is available
        PreparedStatement ps = conn.prepareStatement("SELECT status FROM rooms WHERE room_no=?");
        ps.setInt(1, room);
        ResultSet rs = ps.executeQuery();
        if (rs.next() && rs.getString("status").equalsIgnoreCase("Available")) {
            // Book room
            PreparedStatement book = conn.prepareStatement(
                "INSERT INTO bookings (guest_name, room_no, check_in, check_out) VALUES (?, ?, ?, ?)");
            book.setString(1, guest);
            book.setInt(2, room);
            book.setString(3, checkIn);
            book.setString(4, checkOut);
            book.executeUpdate();

            // Update room status
            PreparedStatement update = conn.prepareStatement(
                "UPDATE rooms SET status='Booked' WHERE room_no=?");
            update.setInt(1, room);
            update.executeUpdate();

            JOptionPane.showMessageDialog(this, "Room booked successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Room is not available!");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Room number must be a valid number.");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Booking Error: " + ex.getMessage());
    }
}


    void checkInRoom() {
        int room = Integer.parseInt(roomField.getText());

        try {
            // Check if there's a booking for this room
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM bookings WHERE room_no=? AND checked_in=FALSE");
            ps.setInt(1, room);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Update booking status to checked in
                PreparedStatement checkIn = conn.prepareStatement(
                    "UPDATE bookings SET checked_in=TRUE WHERE room_no=? AND checked_in=FALSE");
                checkIn.setInt(1, room);
                checkIn.executeUpdate();

                // Update room status
                PreparedStatement update = conn.prepareStatement(
                    "UPDATE rooms SET status='Occupied' WHERE room_no=?");
                update.setInt(1, room);
                update.executeUpdate();

                JOptionPane.showMessageDialog(this, "Check-in successful.");
            } else {
                JOptionPane.showMessageDialog(this, "No pending booking for this room.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Check-in Error: " + ex.getMessage());
        }
    }

    void checkOutRoom() {
        int room = Integer.parseInt(roomField.getText());

        try {
            // Set room back to available
            PreparedStatement update = conn.prepareStatement(
                "UPDATE rooms SET status='Available' WHERE room_no=?");
            update.setInt(1, room);
            update.executeUpdate();

            JOptionPane.showMessageDialog(this, "Check-out successful. Room is now available.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Check-out Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new HotelBookingSystem();
    }
}
