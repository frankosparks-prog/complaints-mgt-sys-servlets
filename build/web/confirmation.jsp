<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Complaint Submitted</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background: #f8f9fa;
            padding: 50px;
        }
        .container {
            background: white;
            padding: 30px;
            width: 400px;
            margin: auto;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            border-radius: 10px;
        }
        h2 {
            color: #28a745;
        }
        a {
            display: block;
            margin-top: 15px;
            padding: 10px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        a:hover {
            background: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Your complaint has been submitted successfully!</h2>
        <p>We will review your complaint and get back to you.</p>
        <a href="welcome.jsp">Submit Another Complaint</a>
    </div>
</body>
</html>
