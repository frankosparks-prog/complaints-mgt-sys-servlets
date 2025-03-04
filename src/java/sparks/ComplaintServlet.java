//package sparks;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
///**
// * Servlet to handle complaint submissions.
// */
//public class ComplaintServlet extends HttpServlet {
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        // Retrieve form data
//        String name = request.getParameter("name");
//        String regNumber = request.getParameter("regNumber");
//        String halls = request.getParameter("halls");
//        String block = request.getParameter("block");
//        String room = request.getParameter("roomno");
//        String complaintText = request.getParameter("complaintText");
//
//        // Ensure session is active
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("username") == null) {
//            response.sendRedirect("login.html"); // Redirect to login if session is invalid
//            return;
//        }
//
//        // Define a fixed directory for complaint storage
//        String directoryPath = "C:/complaints/"; // Windows path (Change to "/opt/complaints/" for Linux)
//        String filePath = directoryPath + "complaints.txt";
//
//        File dir = new File(directoryPath);
//        if (!dir.exists()) {
//            dir.mkdirs(); // Create directory if it does not exist
//        }
//
//        // Log file path for debugging
//        System.out.println("Saving complaint to: " + filePath);
//
//        // Save complaint details to a text file
//        try (FileWriter fw = new FileWriter(filePath, true);
//             PrintWriter pw = new PrintWriter(fw)) {
//            pw.println("=================================");
//            pw.println("Name: " + name);
//            pw.println("Registration Number: " + regNumber);
//            pw.println("Hall: " + halls);
//            pw.println("Block: " + block);
//            pw.println("Room: " + room);
//            pw.println("Complaint: " + complaintText);
//            pw.println("=================================\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving complaint.");
//            return;
//        }
//
//        // Redirect to a confirmation page
//        response.sendRedirect("confirmation.jsp");
//    }
//}


package sparks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle complaint submissions.
 */
public class ComplaintServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form data
        String regNo = request.getParameter("regNo");
        String halls = request.getParameter("halls");
        String block = request.getParameter("block");
        String room = request.getParameter("roomno");
        String complaintText = request.getParameter("complaintText");

        // Ensure session is active and validate student ID
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            response.sendRedirect("login.html"); // Redirect to login if session is invalid
            return;
        }

        // Get student ID from session
        int sessionStudentId = (int) session.getAttribute("student_id");

        // Database connection details
        String dbURL = "jdbc:mysql://localhost:3306/complains_db";
        String dbUser = "Admin";
        String dbPassword = "admin";

        // SQL query to insert complaint
        String sql = "INSERT INTO complaints (description, student_id, halls, block, roomno) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
            // Validate that the provided regNo corresponds to the student_id in the session
            int studentId = getStudentIdByRegNumber(regNo, conn);

            if (studentId == -1) {
                // Redirect to the error page if student not found
                response.sendRedirect("RegError.jsp?message=Invalid%20registration%20number.%20Please%20try%20again.");
                return;
            }

            // Check if the regNo matches the student_id from session
            if (studentId != sessionStudentId) {
                // Redirect to the error page if registration number doesn't match
                response.sendRedirect("RegError.jsp?message=Registration%20number%20does%20not%20match%20the%20logged-in%20student.");
                return;
            }

            // Prepare the SQL statement
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, complaintText); // Set complaint description
                stmt.setInt(2, studentId); // Set student ID
                stmt.setString(3, halls); // Set halls
                stmt.setString(4, block); // Set block
                stmt.setString(5, room); // Set room number

                // Execute the insert
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Redirect to a confirmation page
                    response.sendRedirect("confirmation.jsp");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving complaint.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
        }
    }

    // Method to get student ID based on the registration number
    private int getStudentIdByRegNumber(String regNo, Connection conn) throws SQLException {
        String sql = "SELECT student_id FROM students WHERE regNo = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, regNo); // Assuming regNumber is stored in the students table
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("student_id");
            }
            return -1; // Student not found
        }
    }
}



//package sparks;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
///**
// * Servlet to handle complaint submissions.
// */
//public class ComplaintServlet extends HttpServlet {
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        // Retrieve form data
////        String name = request.getParameter("name");
//        String regNo = request.getParameter("regNo");
//        String halls = request.getParameter("halls");
//        String block = request.getParameter("block");
//        String room = request.getParameter("roomno");
//        String complaintText = request.getParameter("complaintText");
//
//        // Ensure session is active
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("username") == null) {
//            response.sendRedirect("login.html"); // Redirect to login if session is invalid
//            return;
//        }
//
//        // Database connection details
//        String dbURL = "jdbc:mysql://localhost:3306/complains_db";
//        String dbUser = "Admin";
//        String dbPassword = "admin";
//
//         // SQL query to insert complaint
//        String sql = "INSERT INTO complaints (description, student_id, halls, block, roomno) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPassword)) {
//            // Get student ID (for this example, we are using regNumber as student_id, but you should use a proper lookup)
//            int studentId = getStudentIdByRegNumber(regNo, conn); // Add this method to retrieve student ID from the database
//
//            if (studentId == -1) {
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Student not found.");
//                return;
//            }
//
//            // Prepare the SQL statement
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, complaintText); // Set complaint description
//                stmt.setInt(2, studentId); // Set student ID
//                stmt.setString(3, halls); // Set halls
//                stmt.setString(4, block); // Set block
//                stmt.setString(5, room); // Set room number
//
//                // Execute the insert
//                int rowsAffected = stmt.executeUpdate();
//                if (rowsAffected > 0) {
//                    // Redirect to a confirmation page
//                    response.sendRedirect("confirmation.jsp");
//                } else {
//                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving complaint.");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error.");
//        }
//    }
//
//    // Method to get student ID based on the registration number
//    private int getStudentIdByRegNumber(String regNo, Connection conn) throws SQLException {
//        String sql = "SELECT student_id FROM students WHERE regNo = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, regNo); // Assuming regNumber is stored as contact in the students table
//            var resultSet = stmt.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("student_id");
//            }
//            return -1; // Student not found
//        }
//    }
//}
