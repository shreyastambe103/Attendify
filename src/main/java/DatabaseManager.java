import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/attendify";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Bazinga103";

    public static void signUpUser(String phone, String password, String name, String email, String role, String studentID, String className, String subject) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Insert into user table
            String insertUserQuery = "INSERT INTO user (phone_no, password, name, email, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement userStatement = conn.prepareStatement(insertUserQuery);
            userStatement.setString(1, phone);
            userStatement.setString(2, password);
            userStatement.setString(3, name);
            userStatement.setString(4, email);
            userStatement.setString(5, role);
            userStatement.executeUpdate();

            // If the account is a student, insert into the student table
            if (role.equals("Student")) {
                String insertStudentQuery = "INSERT INTO student (studentID) VALUES (?)";
                PreparedStatement studentStatement = conn.prepareStatement(insertStudentQuery);
                studentStatement.setString(1, studentID);
                studentStatement.executeUpdate();

                // Insert into studies table to associate student with class and subject
                String insertStudiesQuery = "INSERT INTO studies (studentID, class, subject) VALUES (?, ?, ?)";
                PreparedStatement studiesStatement = conn.prepareStatement(insertStudiesQuery);
                studiesStatement.setString(1, studentID);
                studiesStatement.setString(2, className);
                studiesStatement.setString(3, subject);
                studiesStatement.executeUpdate();
            }

            // If the account is a teacher, insert into the teacher table
            if (role.equals("Teacher")) {
                String insertTeacherQuery = "INSERT INTO teacher (teacherID) VALUES (?)";
                PreparedStatement teacherStatement = conn.prepareStatement(insertTeacherQuery);
                teacherStatement.setString(1, phone); // Assuming phone number as teacherID for simplicity
                teacherStatement.executeUpdate();
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL errors here
        }
    }
}
