import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

class QuizApp extends JFrame implements ActionListener {
    JLabel questionLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup group;
    JButton nextBtn;
    int currentQuestion = 0, score = 0;
    List<Question> questionList;
    int timeLeft = 30;
    Timer timer;

    public QuizApp(String username) {
        setTitle("Quiz App");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        questionLabel = new JLabel("Question");
        questionLabel.setBounds(20, 20, 450, 30);

        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setBounds(30, 60 + i * 30, 400, 30);
            group.add(options[i]);
            add(options[i]);
        }

        nextBtn = new JButton("Next");
        nextBtn.setBounds(180, 200, 100, 30);
        nextBtn.addActionListener(this);

        add(questionLabel);
        add(nextBtn);
        setLayout(null);
        setVisible(true);

        loadQuestions();
        displayQuestion();

        timer = new Timer(1000, e -> {
            timeLeft--;
            setTitle("Time Left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                timer.stop();
                finishQuiz(username);
            }
        });
        timer.start();
    }

    void loadQuestions() {
        questionList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {
            while (rs.next()) {
                questionList.add(new Question(
                    rs.getString("question"),
                    rs.getString("option1"),
                    rs.getString("option2"),
                    rs.getString("option3"),
                    rs.getString("option4"),
                    rs.getInt("correct_option")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void displayQuestion() {
        if (currentQuestion >= questionList.size()) {
            finishQuiz(null);
            return;
        }
        Question q = questionList.get(currentQuestion);
        questionLabel.setText((currentQuestion + 1) + ". " + q.question);
        options[0].setText(q.option1);
        options[1].setText(q.option2);
        options[2].setText(q.option3);
        options[3].setText(q.option4);
        group.clearSelection();
    }

    public void actionPerformed(ActionEvent e) {
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selected = i + 1;
            }
        }

        if (selected == questionList.get(currentQuestion).correctOption) {
            score++;
        }
        currentQuestion++;
        displayQuestion();
    }

    void finishQuiz(String username) {
        JOptionPane.showMessageDialog(this, "Quiz Over! Score: " + score);
        timer.stop();
        if (username != null) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO scores (username, score) VALUES (?, ?)");
                ps.setString(1, username);
                ps.setInt(2, score);
                ps.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog("Enter your name:");
        new QuizApp(name);
    }
}

class Question {
    String question, option1, option2, option3, option4;
    int correctOption;

    public Question(String question, String o1, String o2, String o3, String o4, int correct) {
        this.question = question;
        this.option1 = o1;
        this.option2 = o2;
        this.option3 = o3;
        this.option4 = o4;
        this.correctOption = correct;
    }
}
