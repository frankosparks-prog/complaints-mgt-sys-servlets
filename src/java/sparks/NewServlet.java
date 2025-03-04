

//
//package sparks;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
///**
// * Servlet handling login authentication.
// */
//public class NewServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        if ("user".equals(username) && "password123".equals(password)) {
//            // Store username in session
//            HttpSession session = request.getSession();
//            session.setAttribute("username", username);
//
//            // Redirect to welcome page
//            response.sendRedirect("welcome.jsp");
//        } else {
//            displayErrorPage(response);
//        }
//    }
//
//    private void displayErrorPage(HttpServletResponse response) throws IOException {
//        try (PrintWriter out = response.getWriter()) {
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Login Failed</title>");
//            out.println("<style>");
//            out.println("body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; background-color: #f8d7da; }");
//            out.println("h2 { color: #721c24; }");
//            out.println("a { padding: 10px 20px; background-color: #dc3545; color: white; text-decoration: none; border-radius: 5px; }");
//            out.println("a:hover { background-color: #c82333; }");
//            out.println("</style>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h2>Invalid username or password</h2>");
//            out.println("<a href='index.html'>Try Again</a>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Login Servlet";
//    }
//}

package sparks;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NewServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/complains_db";
    private static final String DB_USER = "Admin";
    private static final String DB_PASS = "admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if ("signup".equals(action)) {
            handleSignup(request, response);
        } else if ("login".equals(action)) {
            handleLogin(request, response);
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String regNo = request.getParameter("regNo");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Ensure JDBC driver is loaded

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "INSERT INTO students (first_name, last_name, contact, email, password, regNo) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, firstName);
                    stmt.setString(2, lastName);
                    stmt.setString(3, contact);
                    stmt.setString(4, email);
                    stmt.setString(5, hashPassword(password));  // Store hashed password
                    stmt.setString(6, regNo);

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        response.sendRedirect("index.html");
                    } else {
                        displayErrorPage(response, "Signup failed. Please try again.");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            displayErrorPage(response, "Database error occurred. Please try again.");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String regNo = request.getParameter("regNo");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "SELECT * FROM students WHERE regNo = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, regNo);
                    stmt.setString(2, hashPassword(password));  // Compare hashed password

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            HttpSession session = request.getSession();
                            session.setAttribute("username", rs.getString("first_name") + " " + rs.getString("last_name"));
                            session.setAttribute("student_id", rs.getInt("student_id"));
                            response.sendRedirect("welcome.jsp");
                        } else {
                            displayErrorPage(response, "Invalid registration number or password.");
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            displayErrorPage(response, "Database error occurred. Please try again.");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

  private void displayErrorPage(HttpServletResponse response, String errorMessage) throws IOException {
    response.setContentType("text/html");
    try (PrintWriter out = response.getWriter()) {
        out.println("<html><head><title>Error</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f8d7da; text-align: center; padding: 50px; }");
        out.println(".error-container { background: white; padding: 20px; border-radius: 8px; display: inline-block; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }");
        out.println("h2 { color: #721c24; }");
        out.println("a { text-decoration: none; color: #0056b3; font-weight: bold; }");
        out.println("a:hover { text-decoration: underline; }");
        out.println("</style>");
        out.println("</head><body>");
        out.println("<div class='error-container'>");
        out.println("<h2>" + errorMessage + "</h2>");
        out.println("<a href='login.html'>Go Back</a>");
        out.println("</div>");
        out.println("</body></html>");
    }
}


    @Override
    public String getServletInfo() {
        return "Signup and Login Servlet";
    }
}
//
//package sparks;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.*;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
///**
// * Servlet handling login authentication and signup logic.
// */
//public class NewServlet extends HttpServlet {
//
//    // JDBC details (Update these with your MySQL setup)
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/complains_db";
//    private static final String DB_USER = "Admin";
//    private static final String DB_PASS = "admin";
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//
//        String action = request.getParameter("action"); // Check if it's login or signup
//        if ("signup".equals(action)) {
//            handleSignup(request, response);
//        } else if ("login".equals(action)) {
//            handleLogin(request, response);
//        }
//    }
//
//    // Handle user signup
//    private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String firstName = request.getParameter("first_name");
//        String lastName = request.getParameter("last_name");
//        String contact = request.getParameter("contact");
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//        String regNo = request.getParameter("regNo");
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
//            String query = "INSERT INTO students (first_name, last_name, contact, email, password, regNo) VALUES (?, ?, ?, ?, ?, ?)";
//            try (PreparedStatement stmt = conn.prepareStatement(query)) {
//                stmt.setString(1, firstName);
//                stmt.setString(2, lastName);
//                stmt.setString(3, contact);
//                stmt.setString(4, email);
//                stmt.setString(5, password);  // Hash later
//                stmt.setString(6, regNo);
//                
//                int rowsAffected = stmt.executeUpdate();
//                if (rowsAffected > 0) {
//                    response.sendRedirect("index.html"); // Redirect to login page on successful signup
//                } else {
//                    displayErrorPage(response, "Signup failed. Please try again.");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("SQL Error: " + e.getMessage());  // Log the error message
//            displayErrorPage(response, "Database error occurred. Please try again.");
//        }
//    }
//
//    // Handle user login
//private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//    String regNo = request.getParameter("regNo");  // registration number)
//    String password = request.getParameter("password");  // Password
//
//    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
//        // Modified query: now we check for regNo and password (no hashing)
//        String query = "SELECT * FROM students WHERE regNo = ? AND password = ?";
//        try (PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setString(1, regNo);  // Set the registration number
//            stmt.setString(2, password);  // Use the plain text password
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    // Login successful
//                    HttpSession session = request.getSession();
//                    session.setAttribute("username", rs.getString("first_name") + " " + rs.getString("last_name"));
//                    response.sendRedirect("welcome.jsp");  // Redirect to welcome page
//                } else {
//                    displayErrorPage(response, "Invalid registration number or password.");
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        displayErrorPage(response, "Database error occurred. Please try again.");
//    }
//}
//
//
//    // Display error page
//    private void displayErrorPage(HttpServletResponse response, String errorMessage) throws IOException {
//        try (PrintWriter out = response.getWriter()) {
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Error</title>");
//            out.println("<style>");
//            out.println("body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; background-color: #f8d7da; }");
//            out.println("h2 { color: #721c24; }");
//            out.println("a { padding: 10px 20px; background-color: #dc3545; color: white; text-decoration: none; border-radius: 5px; }");
//            out.println("a:hover { background-color: #c82333; }");
//            out.println("</style>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h2>" + errorMessage + "</h2>");
//            out.println("<a href='index.html'>Go Back</a>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Signup and Login Servlet with student_id authentication";
//    }
//}
