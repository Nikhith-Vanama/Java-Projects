import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

class CurrencyConverter extends JFrame implements ActionListener {

    private JTextField amountField;
    private JComboBox<String> fromCurrency, toCurrency;
    private JLabel resultLabel;

    // Currency rates relative to USD
    private HashMap<String, Double> currencyRates;

    public CurrencyConverter() {
        setTitle("Currency Converter");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // Initialize exchange rates (you can update these)
        currencyRates = new HashMap<>();
        currencyRates.put("USD", 1.0);
        currencyRates.put("EUR", 0.92);
        currencyRates.put("INR", 83.2);
        currencyRates.put("GBP", 0.79);
        currencyRates.put("JPY", 155.5);

        String[] currencies = currencyRates.keySet().toArray(new String[0]);

        // Amount input
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField(10);
        inputPanel.add(amountField);
        add(inputPanel);

        // From currency
        JPanel fromPanel = new JPanel();
        fromPanel.add(new JLabel("From:"));
        fromCurrency = new JComboBox<>(currencies);
        fromPanel.add(fromCurrency);
        add(fromPanel);

        // To currency
        JPanel toPanel = new JPanel();
        toPanel.add(new JLabel("To:"));
        toCurrency = new JComboBox<>(currencies);
        toPanel.add(toCurrency);
        add(toPanel);

        // Convert button
        JButton convertBtn = new JButton("Convert");
        convertBtn.addActionListener(this);
        add(convertBtn);

        // Result label
        resultLabel = new JLabel("Result: ");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultLabel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = (String) fromCurrency.getSelectedItem();
            String to = (String) toCurrency.getSelectedItem();

            double fromRate = currencyRates.get(from);
            double toRate = currencyRates.get(to);

            double usd = amount / fromRate; // Convert to USD
            double converted = usd * toRate; // Convert to target currency

            resultLabel.setText(String.format("Result: %.2f %s", converted, to));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverter());
    }
}
