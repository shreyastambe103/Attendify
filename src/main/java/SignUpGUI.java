import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpGUI extends JFrame {
    private JPasswordField adminIDField;
    private JComboBox<String> accountTypeComboBox;
    private JTextField nameField;
    private JTextField studentIDField;
    private JTextField classField; // Added class field
    private JTextField subjectField; // Added subject field
    private JTextField emailField; // Added email field
    private JTextField phoneField; // Added phone number field
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignUpGUI() {
        super("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();

        // Set visible
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(33, 33, 33)); // #212121
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("ATTENDIFY");
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // Increased rows to accommodate new fields
        formPanel.setBackground(new Color(61, 61, 61)); // Slightly lighter shade of the background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel adminIDLabel = new JLabel("Admin ID:");
        adminIDLabel.setForeground(Color.WHITE);
        adminIDField = new JPasswordField();
        formPanel.add(adminIDLabel);
        formPanel.add(adminIDField);


        JLabel accountTypeLabel = new JLabel("Account Type:");
        accountTypeLabel.setForeground(Color.WHITE);
        String[] accountTypes = {"Select Account Type", "Student", "Teacher"};
        accountTypeComboBox = new JComboBox<>(accountTypes);
        formPanel.add(accountTypeLabel);
        formPanel.add(accountTypeComboBox);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailField = new JTextField();
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        JLabel phoneLabel = new JLabel("Phone No.:");
        phoneLabel.setForeground(Color.WHITE);
        phoneField = new JTextField();
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel studentIDLabel = new JLabel("Student ID:");
        studentIDLabel.setForeground(Color.WHITE);
        studentIDField = new JTextField();
        formPanel.add(studentIDLabel);
        formPanel.add(studentIDField);

        JLabel classLabel = new JLabel("Class:"); // Added class label
        classLabel.setForeground(Color.WHITE);
        classField = new JTextField();
        formPanel.add(classLabel);
        formPanel.add(classField);

        JLabel subjectLabel = new JLabel("Subject:"); // Added subject label
        subjectLabel.setForeground(Color.WHITE);
        subjectField = new JTextField();
        formPanel.add(subjectLabel);
        formPanel.add(subjectField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordField = new JPasswordField();
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton signUpButton = createHoverButton("Sign Up");
        signUpButton.setBackground(new Color(212, 162, 15)); // #d4a20f
        signUpButton.setForeground(Color.BLACK);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        JButton backButton = createHoverButton("Back to Sign In");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new SignInGUI(); // Open the sign-in window
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(0,0,0));
        buttonPanel.add(signUpButton);
        buttonPanel.add(backButton); // Add the back button
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void signUp() {
        String adminID = adminIDField.getText();
        String accountType = (String) accountTypeComboBox.getSelectedItem();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String name = nameField.getText();
        String studentID = studentIDField.getText();
        String className = classField.getText();
        String subject = subjectField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Perform validation
        // Validate all fields are filled
        if (adminID.isEmpty() || phone.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || accountType.equals("Select Account Type")) {
            // Show error message to the user
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!(adminID.equals("admin123"))){
            JOptionPane.showMessageDialog(this, "Incorrect Admin ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate password and confirm password match
        if (!password.equals(confirmPassword)) {
            // Show error message to the user
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Call the database manager to sign up the user
        DatabaseManager.signUpUser(phone, password, name, email, accountType, studentID, className, subject);

        // Optionally, show success message or navigate to another screen
        JOptionPane.showMessageDialog(this, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        // You can navigate to another screen or close the current window here
    }


    private JButton createHoverButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(212, 162, 15)); // #d4a20f
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true); // Set button to be opaque

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(150, 111, 4)); // #fb7c64
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(212, 162, 15)); // #d4a20f
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpGUI::new);
    }
}
