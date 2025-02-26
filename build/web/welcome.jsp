<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<html>
<head>
    <title>Submit Complaint</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
/*            background: linear-gradient(to right, #4facfe, #00f2fe);*/
/*            height: 100vh;*/
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.6);
            text-align: center;
            width: 600px;
        }
        h1 { color: #333; }
        p { font-size: 18px; color: #555; }
        .input-group { margin-bottom: 1rem; text-align: left; }
        .input-group label { display: block; font-weight: bold; margin-bottom: 5px; }
        .input-group input, .input-group select, textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }
        .submit-btn {
            background: #4facfe;
            color: white;
            border: none;
            padding: 10px;
            width: 100%;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            transition: background 0.3s;
        }
        .submit-btn:hover { background: #00c3ff; }
        .logout-btn {
            display: inline-block;
            margin-top: 15px;
            padding: 10px 20px;
            font-size: 16px;
            text-decoration: none;
            color: white;
            background: #ff4757;
            border-radius: 5px;
            transition: background 0.3s ease;
        }
        .logout-btn:hover { background: #e84118; }
    </style>
    <script>
    function updateBlocks() {
        var hall = document.getElementById("halls").value;
        var block = document.getElementById("block");
        block.innerHTML = ""; // Clear existing options

        var blocks = {
            "Tatton": ["Argentina", "Nairobi", "Mombasa"],
            "cbd": ["Naivasha", "Turkana", "Victoria"],
            "maringo": ["maringo"],
            "select": []
        };
        
        //options logic
        if (blocks[hall]) {
            blocks[hall].forEach(function(b) {
                var option = document.createElement("option");
                option.value = b;
                option.textContent = b;
                block.appendChild(option);
            });
        }
    }

    function validateForm(event) {
//        let name = document.getElementById("name").value.trim();
        let regNumber = document.getElementById("regNo").value.trim();
        let hall = document.getElementById("halls").value;
        let block = document.getElementById("block").value;
        let roomNo = document.getElementById("roomno").value.trim();
        let complaint = document.querySelector("textarea[name='complaintText']").value.trim();
        
//        let namePattern = /^[A-Za-z\s]+$/;
        let regPattern = /^[A-Z]\d{2}\/\d{5}\/\d{2}$/; // Matches `S13/07798/22`
        let roomPattern = /^\d+$/;

//        if (!namePattern.test(name)) {
//            alert("Invalid name. Only letters and spaces are allowed.");
//            event.preventDefault();
//            return false;
//        }

        if (!regPattern.test(regNumber)) {
            alert("Invalid Registration Number. Format: S13/07798/22");
            event.preventDefault();
            return false;
        }

        if (hall === "select") {
            alert("Please select a Hall.");
            event.preventDefault();
            return false;
        }

        if (block === "" || block === "--Select Block--") {
            alert("Please select a Block.");
            event.preventDefault();
            return false;
        }

        if (!roomPattern.test(roomNo)) {
            alert("Invalid Room Number. Only numbers are allowed.");
            event.preventDefault();
            return false;
        }

        if (complaint.length < 10) {
            alert("Complaint should be at least 10 characters long.");
            event.preventDefault();
            return false;
        }

        return true;
    }
</script>

</head>
<body>
    <div class="container">
        <h1>Welcome, <%= session.getAttribute("username") != null ? session.getAttribute("username") : "Guest" %>!</h1>
        <p>You have successfully logged in. Fill the form below!</p>
        <form action="/WebApplication1/ComplaintServlet" method="post" onsubmit="return validateForm(event)">
<!--            <div class="input-group">
                <label for="name">Name</label>
                <input type="text" id="name" name="name" placeholder="John Doe" required>
            </div>-->
            <div class="input-group">
                <label for="regNo">Registration Number</label>
                <input type="text" id="regNo" name="regNo" placeholder="S13/07798/22" required>
            </div>
            <div class="input-group">
                <label for="halls">Halls</label>
                <select id="halls" name="halls" onchange="updateBlocks()">
                    <option value="select">--Select Hall--</option>
                    <option value="Tatton">Tatton</option>
                    <option value="cbd">CBD</option>
                    <option value="maringo">Maringo</option>>
                </select>
            </div>
            <div class="input-group">
                <label for="block">Block</label>
                <select id="block" name="block">
                    <option value="">--Select Block--</option>
                </select>
            </div>
            <div class="input-group">
                <label for="room">Room</label>
                <input type="text" id="roomno" name="roomno" placeholder="02" required>
            </div>
            <div class="input-group">
                <label for="complaint">Complaint</label>
                <textarea name="complaintText" rows="4" placeholder="Enter your complaint..." required></textarea>
            </div>
            <button type="submit" class="submit-btn">Submit Complaint</button>
        </form>
        <a href="index.html" class="logout-btn">Logout</a>
    </div>
</body>
</html>
