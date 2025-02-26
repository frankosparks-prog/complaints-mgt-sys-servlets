//package sparks;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class DatabaseTestServlet extends HttpServlet {
//
//    // JDBC details (update these with your MySQL configuration)
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/complains_db";
//    private static final String DB_USER = "Admin";  // Your DB username
//    private static final String DB_PASS = "admin";  // Your DB password
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("text/html;charset=UTF-8");
//
//        try (PrintWriter out = response.getWriter()) {
//            // Attempt to connect to the database
//            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
//                out.println("<h2>Database connection successful!</h2>");
//            } catch (SQLException e) {
//                out.println("<h2>Database connection failed.</h2>");
//                out.println("<p>Error: " + e.getMessage() + "</p>");
//            }
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Simple Database Connection Test Servlet";
//    }
//}
