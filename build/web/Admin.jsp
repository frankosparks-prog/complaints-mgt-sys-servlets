<%@ page import="java.sql.*" %>
<html>
<head>
    <title>Complaints Dashboard</title>
    <style>
        /* Reset margins and padding for all elements */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f7fb;
            color: #333;
            padding: 20px;
        }

        h2 {
            font-size: 2rem;
            color: #333;
            margin-bottom: 20px;
            font-weight: bold;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            padding: 15px;
            text-align: left;
            border: 1px solid #ddd;
        }

        th {
            background-color: #007BFF;
            color: white;
            font-size: 1.1rem;
            text-transform: uppercase;
        }

        td {
            background-color: #fff;
            color: #555;
            font-size: 1rem;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        select, button {
            padding: 8px 12px;
            font-size: 1rem;
            border-radius: 5px;
            border: 1px solid #ddd;
            background-color: #fff;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        select:focus, button:focus {
            outline: none;
            border-color: #007BFF;
        }

        button {
            background-color: #007BFF;
            color: white;
            border: none;
            margin-top: 5px;
        }

        button:hover {
            background-color: #0056b3;
        }

        /* Styling for status dropdown */
        select {
            width: 120px;
            margin-right: 10px;
        }

        form {
            display: inline;
        }

        /* Responsive styling for smaller screens */
        @media screen and (max-width: 768px) {
            h2 {
                font-size: 1.5rem;
            }

            table, th, td {
                font-size: 0.9rem;
            }

            select, button {
                font-size: 0.9rem;
                padding: 6px 10px;
            }
        }
    </style>
</head>
<body>
    <h2>Complaints List Dashboard</h2>
    <table>
        <tr>
            <th>ID</th>
            <th>Student ID</th>
            <th>Description</th>
            <th>Submitted At</th>
            <th>Status || Action</th>
        </tr>

        <%
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/complains_db", "Admin", "admin");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM complaints");

                while (rs.next()) {
        %>
        <tr>
            <td><%= rs.getInt("complain_id") %></td>
            <td><%= rs.getInt("student_id") %></td>
            <td><%= rs.getString("description") %></td>
            <td><%= rs.getTimestamp("created_at") %></td>
            <td>
                <form action="UpdateComplaintServlet" method="POST">
                    <input type="hidden" name="complain_id" value="<%= rs.getInt("complain_id") %>">
                    <select name="status">
                        <option value="Pending" <%= rs.getString("status").equals("Pending") ? "selected" : "" %>>Pending</option>
                        <option value="Solved" <%= rs.getString("status").equals("Solved") ? "selected" : "" %>>Solved</option>
                        <option value="Rejected" <%= rs.getString("status").equals("Rejected") ? "selected" : "" %>>Rejected</option>
                    </select>
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
        <%
                }
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        %>

    </table>
</body>
</html>
