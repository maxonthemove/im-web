<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <style>

        body {
            background-color: #e3e3e3;
        }

        .box {
            display: flex;
            flex-direction: column;
            flex-wrap: nowrap;
            /* align-content: center; */
            justify-content: flex-start;
            align-items: center;
            overflow: hidden;
        }

        .chatBox {
            display: flex;
            padding: 10px 20px;
            flex-direction: column;
            flex-wrap: nowrap;
            align-content: center;
            justify-content: flex-start;
            align-items: center;
        }

        .messageItem {
            padding: 0px 5px;
            margin: 5px 0;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
            align-content: flex-start;
            justify-content: flex-start;
        }

        .messageHeader {
        }

        .messageHeaderText {
            background-color: burlywood;
            border-radius: 10px;
            padding: 5px;
        }

        .messageBody {
            margin-left: 20px;
            background-color: white;
            padding: 5px;
            border-radius: 5px;
            max-width: 300px;
            word-wrap: break-word;
        }

        .messageBodySelf {
            margin-left: 20px;
            background-color: #96ec68;
            padding: 5px;
            border-radius: 5px;
            max-width: 300px;
            word-wrap: break-word;
        }

        .imgMessage {
            max-width: 300px;
            max-height: 300px;
        }

        .timestamp {
            font-size: 10px;
            color: gray;
            margin-left: 20px;
        }

        @media screen and (max-width: 600px) {
            /* For mobile phones: */
            .messageBody {
                margin-left: 3vw;
                background-color: white;
                padding: 5px;
                border-radius: 5px;
                max-width: 65vw;
                word-wrap: break-word;
            }

            .messageBodySelf {
                margin-left: 3vw;
                background-color: #96ec68;
                padding: 5px;
                border-radius: 5px;
                max-width: 65vw;
                word-wrap: break-word;
            }

            .timestamp {
                font-size: 10px;
                color: gray;
                margin-left: 3vw;
            }

            .imgMessage {
                max-width: 65vw;
                max-height: 65vh;
            }
        }
    </style>
</head>
<body>

<div class="box">
    <div style="font-weight: bold;font-size: 20px">
        Login As: <span id="usernameText"></span>
    </div>
    <div style="position: absolute;top: 5px;right: 10px;">
        <button style="font-size: 15px;font-weight:bold;border: none;background-color: forestgreen;height: 30px;color: white;border-radius: 3px;" onclick="getMessage()">refresh</button>
    </div>
    <hr style="width: 100%;height: 1px;background-color: #000;border: none">
    <div class="chatBox">
        <div id="chatBox" style="max-width: 500px;width: 94vw;overflow: auto;"></div>
    </div>
    <div style="position: absolute;bottom: 0;width: 100vw;background-color: antiquewhite;display: flex;flex-direction: row;justify-content: center;">
        <div style="max-width:500px;display: flex;flex-direction: row;flex-wrap: nowrap;align-content: center;justify-content: center;align-items: center;padding:20px 0px">
            <div id="imgBox">
                <button style="font-weight: bold;height: 50px;font-size: 20px; background-color: forestgreen;border:none;color: #e3e3e3 ;line-height: 20px;border-radius: 3px" onclick="uploadImage()">
                    img
                </button>
            </div>
            <textarea type="text" id="message" style="margin-left: 5px;max-width: 495px;width: 50vw;font-size: 20px;height: 50px;border:none;border-radius: 3px"></textarea>
            <div id="sendBox" style="width: 15vw;margin-left: 5px; width: 60px;height: 50px;display: flex; flex-direction: column;
    justify-content: flex-start;flex-wrap: nowrap;">
                <button style="font-weight: bold;width:60px;height: 50px;font-size: 20px; background-color: forestgreen;border:none;color: #e3e3e3 ;border-radius: 3px" onclick="sendMessage()">send
                </button>
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

    // 设置聊天框高度
    document.getElementById("chatBox").style.height = document.documentElement.clientHeight - 160 + "px";

    // 设置 输入框 有焦点时，输入到第二行，自动增加高度
    document.getElementById("message").addEventListener("input", function () {
        console.log(this.scrollHeight);
        this.style.height = "auto";
        this.style.height = (this.scrollHeight) + "px";
        document.getElementById("sendBox").style.height = this.style.height;
        document.getElementById("imgBox").style.height = this.style.height;
    });

    // 上传图片
    function uploadImage() {
        let file = document.createElement("input");
        file.type = "file";
        file.accept = "image/*";
        file.onchange = function () {
            let formData = new FormData();
            formData.append("file", file.files[0]);
            let xhr = new XMLHttpRequest();
            xhr.open("POST", "/file/uploadImg", true);
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // let data = JSON.parse(xhr.responseText);
                    // if (data.code === 0) {
                    //     alert("上传成功");
                    // } else {
                    //     alert("上传失败");
                    // }
                    getMessage();
                }
            };
            xhr.send(formData);
        };
        file.click();
    }


    function getCookie(name) {
        var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
        if (arr = document.cookie.match(reg)) {
            return unescape(arr[2]);
        } else {
            return null;
        }
    }

    let messageList = [];

    function getMessage() {
        // 发起get请求，获取新消息
        let xhr = new XMLHttpRequest();
        xhr.open("GET", "/chat/getMessage?channel=default", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                if (data && data.length > 0) {
                    if (messageList.length == data.length) {
                        return;
                    }
                    let chatBox = document.getElementById("chatBox");
                    for (let i = 0; i < data.length; i++) {
                        if (i < messageList.length) {
                            continue;
                        }
                        let div = document.createElement("div");
                        let innerHTML = '<div class="messageItem"><div class="messageHeader"> <div class="messageHeaderText">' + data[i].username + "</div></div>";
                        if (data[i].username === username) {
                            if (data[i].messageType == 'text') {
                                innerHTML += "<div class='messageBodyBox'><div class='messageBodySelf'> " + data[i].message + '</div>' + '<div class="timestamp">' + new Date(data[i].timestamp).format('yyyy-MM-dd hh:mm:ss') + '</div>' + '</div>';
                            } else if (data[i].messageType == 'img') {
                                innerHTML += "<div class='messageBodyBox'><div class='messageBodySelf'> <img src='" + data[i].message + "' class='imgMessage'></div>" + '<div class="timestamp">' + new Date(data[i].timestamp).format('yyyy-MM-dd hh:mm:ss') + '</div>' + '</div>';
                            }
                        } else {
                            if (data[i].messageType == 'text') {
                                innerHTML += "<div class='messageBodyBox'><div class='messageBody'>" + data[i].message + '</div>' + '<div class="timestamp">' + new Date(data[i].timestamp).format('yyyy-MM-dd hh:mm:ss') + '</div>' + '</div>';
                            } else if (data[i].messageType == 'img') {
                                innerHTML += "<div class='messageBodyBox'><div class='messageBody'> <img src='" + data[i].message + "' class='imgMessage'></div>" + '<div class="timestamp">' + new Date(data[i].timestamp).format('yyyy-MM-dd hh:mm:ss') + '</div>' + '</div>';
                            }
                        }
                        div.innerHTML = innerHTML;
                        // 将 div 添加到 chatBox
                        chatBox.appendChild(div);
                    }
                    messageList = data;
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