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

        .chatBox{
            display: flex;
            padding: 10px 20px;
            flex-direction: column;
            flex-wrap: nowrap;
            align-content: center;
            justify-content: flex-start;
            align-items: center;
        }
    </style>
</head>
<body>

<div class="box">
    <div style="margin-top: 20px;font-weight: bold;margin-bottom: 20px;font-size: 20px">
        Login As: <span id="usernameText"></span>
    </div>
    <div class="chatBox">
        <div id="chatBox" style="max-width: 500px;width: 90vw;max-height: 500px;height: 80vh;overflow: auto;border: 1px solid #000;border-radius: 10px"></div>
        <div style="display: flex;flex-direction: column;flex-wrap: nowrap;align-content: center;justify-content: center;align-items: center;">
            <textarea type="text" id="message" style="max-width: 495px;width: 90vw;margin-top: 10px;font-size: 20px;border-radius: 10px"></textarea>
            <div style="margin-top: 10px;margin-top: 10px; display: flex;flex-direction: row;justify-content: space-around;
                    flex-wrap: nowrap;align-items: center;max-width: 495px;width: 90vw;">
                <button style="width: 150px;font-weight: bold;height: 35px;font-size: 20px;border-radius: 10px" onclick="getMessage()">Refresh</button>
                <button style="width: 150px;font-weight: bold;height: 35px;font-size: 20px;border-radius: 10px" onclick="sendMessage()">Send</button>
            </div>
        </div>
    </div>
</div>

<script>
    // 从 cookie 中获取用户名
    let username = getCookie("username");
    if (username) {
        // document.getElementById("username").value = username;
        document.getElementById("usernameText").innerText = username;
        getMessage();
    } else {
        window.location.href = "/login";
    }

    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            return unescape(arr[2]);
        } else {
            return null;
        }
    }

    function getMessage() {
        // 发起get请求，获取新消息
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "/chat/getMessage?channel=default", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                if (data && data.length > 0) {
                    let chatBox = document.getElementById("chatBox");
                    chatBox.innerHTML = "";
                    for (let i = 0; i < data.length; i++) {
                        let div = document.createElement("div");
                        div.innerHTML = new Date(data[i].timestamp).format('yyyy-MM-dd hh:mm:ss') + ' ' + data[i].username + ": " + data[i].message;
                        chatBox.appendChild(div);
                    }
                    chatBox.scrollTop = chatBox.scrollHeight;
                }
            }
        };
        xhr.send();
    }

    function sendMessage() {
        let message = document.getElementById("message").value;
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/chat/sendMessage", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                // let data = JSON.parse(xhr.responseText);
                // if (data.code === 0) {
                //     alert("发送成功");
                // } else {
                //     alert("发送失败");
                // }
                document.getElementById("message").value = "";
                getMessage();
            }
        };
        xhr.send(JSON.stringify({
            channel: "default",
            message: message
        }));
    }

    setInterval(getMessage, 5000);

    /**
     *对Date的扩展，将 Date 转化为指定格式的String
     *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
     *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
     *例子：
     *(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
     *(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
     */
    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }


</script>
</body>
</html>