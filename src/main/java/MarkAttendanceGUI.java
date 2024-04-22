import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MarkAttendanceGUI extends JFrame {

    private JComboBox<String> classComboBox;
    private JComboBox<String> subjectComboBox;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JButton submitButton;
    private JButton goBackButton; // Added go back button
    private JPanel mainPanel;

    public MarkAttendanceGUI() {
        super("Mark Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 15, 15));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));


        JLabel titleLabel = new JLabel("Mark Attendance");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monaco", Font.PLAIN, 36)); // Increased font size
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        optionsPanel.setBackground(new Color(33, 33, 33));

        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setForeground(Color.WHITE);
        classLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        optionsPanel.add(classLabel);

        classComboBox = new JComboBox<>();
        classComboBox.setBackground(Color.WHITE);
        loadClasses();
        classComboBox.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        optionsPanel.add(classComboBox);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setForeground(Color.WHITE);
        subjectLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        optionsPanel.add(subjectLabel);

        subjectComboBox = new JComboBox<>();
        loadSubjects();
        subjectComboBox.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        optionsPanel.add(subjectComboBox);

        JButton markAttendanceButton = new JButton("Mark Attendance");
        markAttendanceButton.setBackground(new Color(212, 162, 15)); // #d4a20f
        markAttendanceButton.addActionListener(e -> startMarkingAttendance());
        markAttendanceButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        optionsPanel.add(markAttendanceButton);

        mainPanel.add(optionsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(33, 33, 33));

        tableModel = new DefaultTableModel(new String[]{"Student ID", "Name", "Present"}, 0);
        studentTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) {
                    return Boolean.class;
                } else {
                    return super.getColumnClass(column);
                }
            }
        };
        studentTable.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        studentTable.setRowHeight(30); // Increased row height
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Increased column width
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(350); // Increased column width
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Increased column width

        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setFont(studentTable.getFont());
        scrollPane.setViewportView(studentTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(33, 33, 33));



        goBackButton = new JButton("Go Back");
        goBackButton.setBackground(new Color(128, 128, 128)); // Gray color
        goBackButton.setForeground(Color.BLACK);
        goBackButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        goBackButton.addActionListener(e -> goBack());
        buttonPanel.add(goBackButton);

        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 176, 0)); // Green color
        submitButton.setForeground(Color.black);
        submitButton.setFont(new Font("Arial", Font.PLAIN, 20)); // Increased font size
        submitButton.addActionListener(e -> submitAttendance());
        buttonPanel.add(submitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadClasses() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Class_Name FROM class")) {
            while (rs.next()) {
                classComboBox.addItem(rs.getString("Class_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSubjects() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SubjectID, Subject_name FROM subject")) {
            while (rs.next()) {
                subjectComboBox.addItem(rs.getInt("SubjectID") + " - " + rs.getString("Subject_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startMarkingAttendance() {
        String selectedClass = classComboBox.getSelectedItem() != null ? classComboBox.getSelectedItem().toString() : "";
        String selectedSubject = subjectComboBox.getSelectedItem() != null ? subjectComboBox.getSelectedItem().toString() : "";

        if (!selectedClass.isEmpty() && !selectedSubject.isEmpty()) {
            List<Student> students = getStudents(selectedClass, selectedSubject);

            if (!students.isEmpty()) {
                tableModel.setRowCount(0);
                for (Student student : students) {
                    Object[] row = {student.getId(), student.getName(), false};
                    tableModel.addRow(row);
                }
                submitButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "No students found for the selected class and subject.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select class and subject.");
        }
    }

    private List<Student> getStudents(String selectedClass, String selectedSubject) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
            String sql = "SELECT s.StudentID, u.Name " +
                    "FROM student s " +
                    "INNER JOIN joins j ON s.StudentID = j.studentid " +
                    "INNER JOIN class c ON j.classid = c.ClassID " +
                    "INNER JOIN studies st ON s.StudentID = st.studentid " +
                    "INNER JOIN subject sub ON st.subjectid = sub.SubjectID " +
                    "INNER JOIN teaches t ON sub.SubjectID = t.SubjectID " +
                    "INNER JOIN teacher te ON t.TeacherID = te.TeacherID " +
                    "INNER JOIN staff stf ON te.StaffID = stf.StaffID " +
                    "INNER JOIN user u ON s.UserID = u.UserID " +
                    "WHERE c.Class_Name = ? AND sub.Subject_name = ? AND s.StudentID IS NOT NULL";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedClass);
            stmt.setString(2, selectedSubject.substring(selectedSubject.indexOf("-") + 2).trim());
            rs = stmt.executeQuery();

            while (rs.next()) {
                int studentID = rs.getInt("StudentID");
                String name = rs.getString("Name");
                students.add(new Student(studentID, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return students;
    }

    private void submitAttendance() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit the attendance?", "Confirm Submission", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            int subjectID = Integer.parseInt(subjectComboBox.getSelectedItem().toString().split(" - ")[0]);
            String subjectName = "";

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
                 PreparedStatement stmt = conn.prepareStatement("SELECT Subject_name FROM subject WHERE SubjectID = ?")) {
                stmt.setInt(1, subjectID);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        subjectName = rs.getString("Subject_name");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int studentID = (int) tableModel.getValueAt(i, 0);
                String date = LocalDate.now().toString();
                boolean present = (boolean) tableModel.getValueAt(i, 2);
                String status = present ? "Present" : "Absent";
                String studentName = "";

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
                     PreparedStatement stmt = conn.prepareStatement("SELECT Name FROM user WHERE UserID = (SELECT UserID FROM student WHERE StudentID = ?)")) {
                    stmt.setInt(1, studentID);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            studentName = rs.getString("Name");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendify", "root", "Bazinga103");
                     PreparedStatement stmt = conn.prepareStatement("INSERT INTO attendance (StudentID, StudentName, Date, Status, SubjectID, Subject_name) VALUES (?, ?, ?, ?, ?, ?)")) {
                    stmt.setInt(1, studentID);
                    stmt.setString(2, studentName);
                    stmt.setString(3, date);
                    stmt.setString(4, status);
                    stmt.setInt(5, subjectID);
                    stmt.setString(6, subjectName);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            submitButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Attendance marked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void goBack() {
        // Add code to go back to teacherDashboard
        // For example:
        dispose(); // Close current window
        TeacherDashboardGUI teacherDashboardGUI = new TeacherDashboardGUI();
        teacherDashboardGUI.setVisible(true);
    }
    static class Student {
        private int id;
        private String name;

        public Student(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MarkAttendanceGUI::new);
    }
}
