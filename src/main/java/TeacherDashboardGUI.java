import javax.swing.*;
import java.awt.*;

public class TeacherDashboardGUI extends JFrame {
    private long teacherID;
    public TeacherDashboardGUI() {
        super("Teacher Dashboard");
        //this.teacherID = teacherID;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1440, 900);
        setLocationRelativeTo(null);

        // Initialize components
        initComponents();

        // Set visible
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(33, 33, 33)); // #212121

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 15, 15)); // #0f0f0f
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("ATTENDIFY");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 30));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Sidebar panel
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBackground(new Color(51, 51, 51)); // #333333
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight())); // Reduced width
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Sidebar content
        JPanel sidebarContent = new JPanel();
        sidebarContent.setLayout(new BoxLayout(sidebarContent, BoxLayout.Y_AXIS));
        sidebarContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarContent.setBackground(new Color(51, 51, 51)); // #333333

        // Home button
        JButton homeButton = createSidebarButton("Home");
        homeButton.addActionListener(e -> handleMenuItem("Home"));
        sidebarContent.add(homeButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // Mark Attendance button
        JButton markAttendanceButton = createSidebarButton("Mark Attendance");
        markAttendanceButton.addActionListener(e -> markAttendance());
        sidebarContent.add(markAttendanceButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // View Attendance button
        JButton viewAttendanceButton = createSidebarButton("View Attendance");
        viewAttendanceButton.addActionListener(e -> viewAttendance());
        sidebarContent.add(viewAttendanceButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // Logout button
        JButton logoutButton = createSidebarButton("Logout");
        logoutButton.addActionListener(e -> logout());
        sidebarContent.add(logoutButton);
        sidebarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        sidebarPanel.add(sidebarContent, BorderLayout.CENTER);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(33, 33, 33)); // #212121

        // Teacher dashboard content
        JPanel teacherDashboardContent = new JPanel();
        teacherDashboardContent.setBackground(new Color(33, 33, 33)); // #212121
        teacherDashboardContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel dashboardLabel = new JLabel("Teacher Dashboard Content");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardLabel.setForeground(Color.WHITE);
        teacherDashboardContent.add(dashboardLabel);

        mainContentPanel.add(teacherDashboardContent, BorderLayout.CENTER);
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
        button.setOpaque(true); // Set button to be opaque
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding
        button.setBorder(BorderFactory.createLineBorder(new Color(212, 162, 15), 10)); // Border color and width

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
        // Handle menu item click events
        JOptionPane.showMessageDialog(null, "You clicked: " + menuItem);
    }

    private void logout() {
        // Close the current TeacherDashboardGUI instance
        dispose();
        // Open a new SignInGUI instance
        new SignInGUI();
    }
    private void markAttendance(){
        //Close current window
        dispose();
        //open mark attendance page
        new MarkAttendanceGUI();
    }
    private void viewAttendance() {
        // Close current window
        dispose();
        // Open the TeacherViewAttendance
        new TeacherViewAttendance();}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TeacherDashboardGUI::new);
    }
}
