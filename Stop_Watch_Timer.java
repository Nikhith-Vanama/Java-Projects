import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class StopwatchApp extends JFrame {
    private JLabel timeLabel;
    private JButton startButton, pauseButton, resetButton;
    private Timer timer;
    private int elapsedSeconds = 0;
    private boolean isRunning = false;

    public StopwatchApp() {
        setTitle("Java Stopwatch");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Time display
        timeLabel = new JLabel(formatTime(elapsedSeconds), SwingConstants.CENTER);
        timeLabel.setFont(new Font("Verdana", Font.BOLD, 30));
        add(timeLabel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        resetButton = new JButton("Reset");

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Timer logic (tick every 1 sec)
        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            timeLabel.setText(formatTime(elapsedSeconds));
        });

        // Button listeners
        startButton.addActionListener(e -> start());
        pauseButton.addActionListener(e -> pause());
        resetButton.addActionListener(e -> reset());
    }

    private void start() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
        }
    }

    private void pause() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
        }
    }

    private void reset() {
        timer.stop();
        elapsedSeconds = 0;
        timeLabel.setText(formatTime(elapsedSeconds));
        isRunning = false;
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StopwatchApp().setVisible(true);
        });
    }
}
