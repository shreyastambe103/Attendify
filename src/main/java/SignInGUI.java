import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignInGUI() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();

        // Set visible
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(33, 33, 33)); // #212121
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel titleLabel = new JLabel("ATTENDIFY");
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(61, 61, 61)); // Slightly lighter shade of the background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Monaco", Font.PLAIN, 30));
        loginLabel.setForeground(Color.WHITE);
        formPanel.add(loginLabel, gbc);

        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        formPanel.add(usernameLabel, gbc);

        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JButton loginButton = createHoverButton("Login");
        loginButton.setBackground(new Color(212, 162, 15)); // #d4a20f
        loginButton.setForeground(Color.BLACK);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform login action
                performLogin();
            }
        });

        JButton registerButton = createHoverButton("Register");
        registerButton.setBackground(new Color(212, 162, 15)); // #d4a20f
        registerButton.setForeground(Color.BLACK);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current sign-in window
                dispose();
                // Open SignUpGUI page
                new SignUpGUI();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(33, 33, 33)); // #212121
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
            // Prepare SQL statement
            String query = "SELECT * FROM user WHERE Name = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if user exists
            if (resultSet.next()) {
                // Get the role of the user
                String role = resultSet.getString("Role");

                // Open the appropriate dashboard based on the role
                switch (role) {
                    case "admin":
                    case "teacher":
                        // Retrieve the TeacherID if the user is a teacher
                        long teacherID = getLoggedInTeacherID(username);
                        if (teacherID != -1) {
                            new TeacherDashboardGUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "Teacher ID not found.");
                        }
                        break;
                    case "student":
                        new StudentDashboardGUI(); //Open Student Dashboard
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid role for user.");
                }
                dispose(); // Close the login window after opening the dashboard
            } else {
                // Authentication failed, display error message
                JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while attempting to login.");
        }
    }
    private long getLoggedInTeacherID(String username) {
        long teacherID = -1; // Default value in case no teacher is found

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103")) {
            // Prepare SQL statement to select TeacherID based on the provided username
            String query = "SELECT TeacherID FROM teacher WHERE UserID = (SELECT UserID FROM user WHERE Name = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Check if a teacher with the provided username exists
            if (resultSet.next()) {
                teacherID = resultSet.getLong("TeacherID");
            }

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teacherID;
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
        // Load MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(SignInGUI::new);
    }
}
