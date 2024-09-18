<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>
        .box {
            display: flex;
            flex-direction: column;
            flex-wrap: nowrap;
            /* align-content: center; */
            justify-content: flex-start;
            align-items: center;
        }
    </style>
</head>
<body>

<div class="box" >
    <div style="margin-top: 20px;font-weight: bold;margin-bottom: 20px;font-size: 20px">
        Just Type a Username:
    </div>
    <input type="text" id="username" name="username" autocomplete="off" style="height: 30px;font-size: 25px;">
    <button onclick="login()" style="width: 120px;height: 30px;font-size: 20px;font-weight: bold;margin-top: 20px;border-radius: 10px">Login</button>
</div>

<script>
    function login() {
        let username = document.getElementById("username").value;
        if (username) {
            // 写入 cookie
            document.cookie = "username=" + username;
            window.location.href = "/chat";
        } else {
            alert("Please type a username");
        }
    }
</script>
</body>
</html>