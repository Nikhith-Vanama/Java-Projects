import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class QuizApp extends JFrame implements ActionListener {
    String[] questions = {
        "What is the capital of France?",
        "Who developed Java?",
        "Which planet is known as the Red Planet?"
    };
    
    String[][] options = {
        {"Paris", "London", "Berlin", "Madrid"},
        {"James Gosling", "Guido van Rossum", "Bjarne Stroustrup", "Dennis Ritchie"},
        {"Earth", "Jupiter", "Mars", "Venus"}
    };
    
    int[] answers = {0, 0, 2}; // Index of correct options
    int index = 0, correct = 0;
    int timer = 10;
    Timer countdown;

    JLabel questionLabel, timerLabel;
    JRadioButton[] choices = new JRadioButton[4];
    ButtonGroup group;
    JButton nextBtn;

    public QuizApp() {
        setTitle("Quiz App");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top: Timer
        timerLabel = new JLabel("Time Left: 10", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerLabel, BorderLayout.NORTH);

        // Center: Question + Options
        JPanel centerPanel = new JPanel(new GridLayout(5, 1));
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(questionLabel);

        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            choices[i] = new JRadioButton();
            group.add(choices[i]);
            centerPanel.add(choices[i]);
        }

        add(centerPanel, BorderLayout.CENTER);

        // Bottom: Next button
        nextBtn = new JButton("Next");
        nextBtn.addActionListener(this);
        add(nextBtn, BorderLayout.SOUTH);

        loadQuestion();

        countdown = new Timer(1000, e -> {
            timer--;
            timerLabel.setText("Time Left: " + timer);
            if (timer == 0) {
                showNext();
            }
        });
        countdown.start();

        setVisible(true);
    }

    void loadQuestion() {
        questionLabel.setText((index + 1) + ". " + questions[index]);
        for (int i = 0; i < 4; i++) {
            choices[i].setText(options[index][i]);
        }
        group.clearSelection();
        timer = 10;
    }

    void showNext() {
        countdown.stop();
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (choices[i].isSelected()) {
                selected = i;
                break;
            }
        }
        if (selected == answers[index]) {
            correct++;
        }

        index++;
        if (index < questions.length) {
            loadQuestion();
            countdown.start();
        } else {
            showResult();
        }
    }

    void showResult() {
        JOptionPane.showMessageDialog(this, "Quiz Over!\nCorrect Answers: " + correct + " out of " + questions.length);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        showNext();
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}
