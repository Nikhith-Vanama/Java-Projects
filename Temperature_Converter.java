import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TemperatureConverter extends JFrame implements ActionListener {

    private JTextField inputField;
    private JComboBox<String> fromUnit, toUnit;
    private JLabel resultLabel;

    public TemperatureConverter() {
        setTitle("Temperature Converter");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        // Input field
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Temperature:"));
        inputField = new JTextField(10);
        inputPanel.add(inputField);
        add(inputPanel);

        // Unit selectors
        String[] units = { "Celsius", "Fahrenheit", "Kelvin" };
        JPanel unitPanel = new JPanel();
        fromUnit = new JComboBox<>(units);
        toUnit = new JComboBox<>(units);
        unitPanel.add(new JLabel("From:"));
        unitPanel.add(fromUnit);
        unitPanel.add(new JLabel("To:"));
        unitPanel.add(toUnit);
        add(unitPanel);

        // Convert button
        JButton convertButton = new JButton("Convert");
        convertButton.addActionListener(this);
        add(convertButton);

        // Result display
        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel("Result: ");
        resultPanel.add(resultLabel);
        add(resultPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double inputTemp = Double.parseDouble(inputField.getText());
            String from = (String) fromUnit.getSelectedItem();
            String to = (String) toUnit.getSelectedItem();
            double result = convertTemperature(inputTemp, from, to);
            resultLabel.setText(String.format("Result: %.2f %s", result, to));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double convertTemperature(double temp, String from, String to) {
        if (from.equals(to)) return temp;

        // Convert input to Celsius
        double celsius;
        switch (from) {
            case "Fahrenheit":
                celsius = (temp - 32) * 5 / 9;
                break;
            case "Kelvin":
                celsius = temp - 273.15;
                break;
            default:
                celsius = temp;
        }

        // Convert Celsius to target unit
        switch (to) {
            case "Fahrenheit":
                return celsius * 9 / 5 + 32;
            case "Kelvin":
                return celsius + 273.15;
            default:
                return celsius;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TemperatureConverter());
    }
}
