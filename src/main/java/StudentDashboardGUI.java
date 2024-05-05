import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentDashboardGUI extends JFrame {

    private JTextField studentIDField;

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
        JLabel dashboardLabel = new JLabel("Enter Student ID");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 20));
        dashboardLabel.setForeground(Color.WHITE);
        studentDashboardContent.add(dashboardLabel);

        studentIDField = new JTextField(10);
        studentDashboardContent.add(studentIDField);

        JButton seeStatsButton = createSidebarButton("See Stats");
        seeStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seeStatsButtonClicked();
            }
        });
        studentDashboardContent.add(seeStatsButton);

        mainContentPanel.add(studentDashboardContent, BorderLayout.CENTER);
        mainPanel.add(mainContentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void seeStatsButtonClicked() {
        String studentID = studentIDField.getText();

        // JDBC Connection
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
            CallableStatement stmt = conn.prepareCall("{ call CalculateAttendanceStats(?) }");
            stmt.setString(1, studentID);
            ResultSet rs = stmt.executeQuery();

            // Create table model
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnLabel(i + 1);
            }

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(columnNames);

            // Populate table model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            // Create JTable
            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            JOptionPane.showMessageDialog(this, scrollPane, "Attendance Stats for Student ID: " + studentID, JOptionPane.INFORMATION_MESSAGE);

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from database", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
