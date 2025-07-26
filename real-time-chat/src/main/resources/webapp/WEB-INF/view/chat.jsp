<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Legacy Chat</title>
    <script>
        const socket = new WebSocket("ws://localhost:8080/chat");
        socket.onmessage = function(event) {
            const msg = JSON.parse(event.data);
            const el = document.createElement("li");
            el.innerText = `[${msg.sender}] ${msg.content}`;
            document.getElementById("messages").appendChild(el);
        };
        function sendMessage() {
            const sender = document.getElementById("sender").value;
            const content = document.getElementById("content").value;
            const message = { sender, content, type: "TEXT" };
            if (msg.type === "IMAGE") {
                el.innerHTML = `<img src='${msg.attachmentUrl}' width='200'/>`;
            } else if (msg.type === "FILE") {
                el.innerHTML = `<a href='${msg.attachmentUrl}' target='_blank'>Скачать файл</a>`;
            } else {
                el.innerText = `[${msg.sender}] ${msg.content}`;
            }
            socket.send(JSON.stringify(message));
        }
    </script>
</head>
<body>
<h2>JSP Chat</h2>
<input id="sender" placeholder="Имя"/><br/>
<input id="content" placeholder="Сообщение"/>
<button onclick="sendMessage()">Отправить</button>
<ul id="messages"></ul>
</body>
</html>
