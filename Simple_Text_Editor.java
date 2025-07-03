import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JFileChooser fileChooser;
    JMenuItem newItem, openItem, saveItem, exitItem;

    public TextEditor() {
        setTitle("Simple Text Editor");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Text area
        textArea = new JTextArea();
        add(new JScrollPane(textArea));

        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // File chooser
        fileChooser = new JFileChooser();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            textArea.setText("");
        } else if (e.getSource() == openItem) {
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    textArea.read(br, null);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not open file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == saveItem) {
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    textArea.write(bw);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not save file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
