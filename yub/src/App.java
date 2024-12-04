import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame implements ActionListener {
  private JTextField inputField;
  private JPanel buttonPanel;
  private JButton darkModeButton;
  private boolean isDarkMode = false;

  public App() {
    // Set up the frame
    setTitle("Calculator");
    setSize(400, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create input field
    inputField = new JTextField();
    inputField.setFont(new Font("Arial", Font.BOLD, 24));
    inputField.setHorizontalAlignment(JTextField.RIGHT);
    inputField.setEditable(false);
    add(inputField, BorderLayout.NORTH);

    // Create button panel with padding and gap between edge and buttons
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(4, 4, 15, 15)); // Adjusting the gap between buttons

    // Buttons
    String[] buttons = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "C", "0", "=", "+"
    };

    for (String text : buttons) {
      JButton button = new JButton(text);
      button.setFont(new Font("Arial", Font.BOLD, 20));
      button.addActionListener(this);
      button.setPreferredSize(new Dimension(80, 80)); // Button size for oval shape
      button.setBackground(Color.WHITE);
      button.setFocusPainted(false);
      button.setOpaque(true);
      button.setContentAreaFilled(true);
      button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2)); // Border for buttons
      buttonPanel.add(button);
    }
    add(buttonPanel, BorderLayout.CENTER);

    // Create dark mode button panel
    JPanel darkModePanel = new JPanel();
    darkModePanel.setLayout(new BorderLayout());

    darkModeButton = new JButton("Dark Mode");
    darkModeButton.setFont(new Font("Arial", Font.BOLD, 18));
    darkModeButton.addActionListener(this);
    darkModeButton.setPreferredSize(new Dimension(400, 50));
    darkModePanel.add(darkModeButton, BorderLayout.CENTER);

    add(darkModePanel, BorderLayout.SOUTH);

    applyTheme();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();

    if ("0123456789".contains(command)) {
      // Append numbers to the input field
      inputField.setText(inputField.getText() + command);
    } else if ("+-*/".contains(command)) {
      // Append operators to the input field
      inputField.setText(inputField.getText() + " " + command + " ");
    } else if ("=".equals(command)) {
      // Evaluate the expression
      String expression = inputField.getText();
      try {
        double result = evaluateExpression(expression);
        inputField.setText(String.valueOf(result));
      } catch (Exception ex) {
        inputField.setText("Error");
      }
    } else if ("C".equals(command)) {
      // Clear the input field
      inputField.setText("");
    } else if ("Dark Mode".equals(command)) {
      // Toggle Dark Mode
      isDarkMode = !isDarkMode;
      applyTheme();
    }
  }

  private double evaluateExpression(String expression) {
    String[] tokens = expression.split(" ");
    double result = Double.parseDouble(tokens[0]);

    for (int i = 1; i < tokens.length; i += 2) {
      String operator = tokens[i];
      double nextNumber = Double.parseDouble(tokens[i + 1]);

      switch (operator) {
        case "+":
          result += nextNumber;
          break;
        case "-":
          result -= nextNumber;
          break;
        case "*":
          result *= nextNumber;
          break;
        case "/":
          if (nextNumber != 0) {
            result /= nextNumber;
          } else {
            throw new ArithmeticException("Cannot divide by zero");
          }
          break;
      }
    }

    return result;
  }

  private void applyTheme() {
    Color backgroundColor;
    Color textColor;
    Color buttonBackgroundColor;
    Color buttonTextColor;

    if (isDarkMode) {
      backgroundColor = Color.DARK_GRAY;
      textColor = Color.GREEN;
      buttonBackgroundColor = Color.BLACK;
      buttonTextColor = Color.WHITE;
    } else {
      backgroundColor = Color.LIGHT_GRAY;
      textColor = Color.BLACK;
      buttonBackgroundColor = Color.WHITE;
      buttonTextColor = Color.BLACK;
    }

    // Apply theme to the frame
    getContentPane().setBackground(backgroundColor);

    // Apply theme to the input field
    inputField.setBackground(backgroundColor);
    inputField.setForeground(textColor);

    // Apply theme to buttons
    Component[] components = buttonPanel.getComponents();
    for (Component component : components) {
      if (component instanceof JButton) {
        JButton button = (JButton) component;
        button.setBackground(buttonBackgroundColor);
        button.setForeground(buttonTextColor);
      }
    }

    // Style the Dark Mode button
    darkModeButton.setBackground(buttonBackgroundColor);
    darkModeButton.setForeground(buttonTextColor);

    // Refresh UI to apply changes
    SwingUtilities.updateComponentTreeUI(this);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      App calculator = new App();
      calculator.setVisible(true);
    });
  }
}
