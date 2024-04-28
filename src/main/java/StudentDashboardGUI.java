import javax.swing.*;
import java.awt.*;

public class StudentDashboardGUI extends JFrame {

    public StudentDashboardGUI() {
        super("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(33, 33, 33)); // #212121

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 15, 15)); // #0f0f0f
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("ATTENDIFY");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 30));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBackground(new Color(51, 51, 51)); // #333333
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel sidebarContent = new JPanel();
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarContent.setBackground(new Color(51, 51, 51)); // #333333

        JButton homeButton = createSidebarButton("Home");
        homeButton.addActionListener(e -> handleMenuItem("Home"));
        sidebarContent.add(homeButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton viewAttendanceButton = createSidebarButton("View Attendance");
        viewAttendanceButton.addActionListener(e -> StudentViewAttendance());
        sidebarContent.add(viewAttendanceButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton logoutButton = createSidebarButton("Logout");
        logoutButton.addActionListener(e -> logout());
        sidebarContent.add(logoutButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebarPanel.add(sidebarContent, BorderLayout.CENTER);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(33, 33, 33)); // #212121

        JPanel studentDashboardContent = new JPanel();
        studentDashboardContent.setBackground(new Color(33, 33, 33)); // #212121
        studentDashboardContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel dashboardLabel = new JLabel("Student Dashboard Content");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardLabel.setForeground(Color.WHITE);
        studentDashboardContent.add(dashboardLabel);

        mainContentPanel.add(studentDashboardContent, BorderLayout.CENTER);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(51, 51, 51)); // #333333
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setBorder(BorderFactory.createLineBorder(new Color(212, 162, 15), 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(212, 162, 15)); // #d4a20f
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(51, 51, 51)); // #333333
            }
        });

        return button;
    }

    private void handleMenuItem(String menuItem) {
        JOptionPane.showMessageDialog(null, "You clicked: " + menuItem);
    }

    private void logout() {
        dispose();
        new SignInGUI();
    }

    private void StudentViewAttendance() {
        dispose();
        new StudentViewAttendance();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentDashboardGUI::new);
    }
}
