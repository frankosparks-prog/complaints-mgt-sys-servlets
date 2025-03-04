package sparks;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateComplaintServlet", urlPatterns = {"/UpdateComplaintServlet"})
public class UpdateComplaintServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/complains_db";
    private static final String JDBC_USER = "Admin";
    private static final String JDBC_PASSWORD = "admin";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Retrieve parameters from the request
        String complaintIdParam = request.getParameter("complain_id");
        String status = request.getParameter("status");

        if (complaintIdParam == null || status == null) {
            response.sendRedirect("Admin.jsp?error=Invalid request");
            return;
        }

        int complaintId;
        try {
            complaintId = Integer.parseInt(complaintIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("Admin.jsp?error=Invalid complaint ID");
            return;
        }

        // Update the complaint status in the database
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String query = "UPDATE complaints SET status = ? WHERE complain_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, complaintId);
                int rowsUpdated = pstmt.executeUpdate();

                if (rowsUpdated > 0) {
                    response.sendRedirect("Admin.jsp?success=Status updated");
                } else {
                    response.sendRedirect("Admin.jsp?error=Complaint not found");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Admin.jsp?error=Database error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles updating complaint status";
    }
}
