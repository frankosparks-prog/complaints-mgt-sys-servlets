<%@ page import="java.sql.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    // Ensure user is logged in
    HttpSession userSession = request.getSession(false);
    if (userSession == null || userSession.getAttribute("student_id") == null) {
        response.sendRedirect("login.html?error=Please log in first");
        return;
    }

    // Convert session attribute to int
    int studentId = Integer.parseInt(userSession.getAttribute("student_id").toString());

    // Database connection details
    String jdbcURL = "jdbc:mysql://localhost:3306/complains_db";
    String dbUser = "Admin";
    String dbPassword = "admin";

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Complaint History</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f7fb;
            color: #333;
        }

        .cont {
            max-width: 1200px;
            margin: 20px auto;
            padding: 20px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .flex {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        h2 {
            font-size: 2rem;
            color: #333;
        }

        a {
            padding: 10px 20px;
            background-color: #007BFF;
            color: white;
            text-decoration: none;
            font-weight: bold;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        a:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            text-align: left;
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            font-size: 1rem;
        }

        th {
            background-color: #007BFF;
            color: white;
            font-weight: bold;
        }

        td {
            background-color: #fff;
            color: #555;
        }

        tr:hover {
            background-color: #f4f4f4;
        }

        .status {
            font-weight: bold;
            padding: 5px 10px;
            border-radius: 3px;
        }

        .status.pending {
            background-color: #f0ad4e;
            color: white;
        }

        .status.solved {
            background-color: #5bc0de;
            color: white;
        }

        .status.rejected {
            background-color: #d9534f;
            color: white;
        }
    </style>
</head>
<body>
    <div class="cont">
        <div class="flex">
            <h2>Complaint History</h2>
            <a href="welcome.jsp">Back</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Complaint ID</th>
                    <th>Description</th>
                    <th>Time Submitted</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    try {
                        // Establish connection
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

                        // Fetch complaints for the logged-in student
                        String sql = "SELECT complain_id, description, created_at, status FROM complaints WHERE student_id = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, studentId);
                        rs = pstmt.executeQuery();

                        // Display complaints
                        while (rs.next()) {
                %>
                            <tr>
                                <td><%= rs.getInt("complain_id") %></td>
                                <td><%= rs.getString("description") %></td>
                                <td><%= rs.getTimestamp("created_at") %></td>
                                <td>
                                    <span class="status <%= rs.getString("status").toLowerCase() %>">
                                        <%= rs.getString("status") %>
                                    </span>
                                </td>
                            </tr>
                <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // Close resources
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>
