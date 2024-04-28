import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class StudentViewAttendance extends JFrame {

    private JComboBox<Long> studentIDComboBox;
    private JComboBox<String> dateComboBox;
    private DefaultTableModel tableModel;
    private JTable attendanceTable;
    private JButton goBackButton;

    public StudentViewAttendance() {
        super("View Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1440, 900);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(33, 33, 33)); // #212121

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(33, 33, 33)); // #212121

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionsPanel.setBackground(new Color(33, 33, 33));

        JLabel studentIDLabel = new JLabel("Select Student ID:");
        studentIDLabel.setForeground(Color.WHITE);
        studentIDLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        optionsPanel.add(studentIDLabel);

        studentIDComboBox = new JComboBox<>();
        loadStudentIDs();
        studentIDComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        optionsPanel.add(studentIDComboBox);

        JLabel dateLabel = new JLabel("Select Date:");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        optionsPanel.add(dateLabel);

        dateComboBox = new JComboBox<>();
        loadDates();
        dateComboBox.setFont(new Font("Arial", Font.PLAIN, 20));
        optionsPanel.add(dateComboBox);

        JButton viewAttendanceButton = new JButton("View Attendance");
        viewAttendanceButton.addActionListener(e -> fetchAttendance());
        viewAttendanceButton.setFont(new Font("Arial", Font.PLAIN, 20));
        optionsPanel.add(viewAttendanceButton);

        contentPanel.add(optionsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(33, 33, 33));

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Date", "Subject", "Status"}, 0);
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 20));
        attendanceTable.setRowHeight(30);

        JTableHeader tableHeader = attendanceTable.getTableHeader();
        tableHeader.setFont(attendanceTable.getFont());
        scrollPane.setViewportView(attendanceTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(33, 33, 33));

        goBackButton = new JButton("Go Back");
        goBackButton.setBackground(new Color(128, 128, 128));
        goBackButton.setForeground(Color.BLACK);
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 20));
        goBackButton.addActionListener(e -> goBack());
        buttonPanel.add(goBackButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void loadStudentIDs() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103")) {
            String query = "SELECT StudentID FROM student";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    long studentID = resultSet.getLong("StudentID");
                    studentIDComboBox.addItem(studentID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading student IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDates() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103")) {
            String query = "SELECT DISTINCT Date FROM attendance";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String date = resultSet.getString("Date");
                    dateComboBox.addItem(date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading dates: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchAttendance() {
        long selectedStudentID = (long) studentIDComboBox.getSelectedItem();
        String selectedDate = (String) dateComboBox.getSelectedItem();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103")) {
            String query = "SELECT s.StudentID, u.Name, a.Date, su.Subject_name, a.Status " +
                    "FROM attendance a " +
                    "JOIN student s ON a.studentid = s.StudentID " +
                    "JOIN user u ON s.UserID = u.UserID " +
                    "JOIN studies st ON s.StudentID = st.studentid " +
                    "JOIN subject su ON st.subjectid = su.SubjectID " +
                    "WHERE a.Date = ? AND s.StudentID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, selectedDate);
                preparedStatement.setLong(2, selectedStudentID);
                ResultSet resultSet = preparedStatement.executeQuery();
                tableModel.setRowCount(0);
                while (resultSet.next()) {
                    long studentID = resultSet.getLong("StudentID");
                    String name = resultSet.getString("Name");
                    String date = resultSet.getString("Date");
                    String subject = resultSet.getString("Subject_name");
                    String status = resultSet.getString("Status");
                    Object[] rowData = {studentID, name, date, subject, status};
                    tableModel.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goBack() {
        // Close the current ViewAttendanceGUI instance
        dispose();
        // Open a new StudentDashboardGUI instance
        new StudentDashboardGUI();
    }
}
