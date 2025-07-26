package com.smesitejl.ui.vaadin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonFactory;

import java.net.http.WebSocket;

@Route("vaadin-chat")
public class VaadinChatView extends VerticalLayout {

    private final UnorderedList messageList = new UnorderedList();
    private WebSocket webSocket;

    public VaadinChatView() {
        TextField sender = new TextField("Name");
        TextField content = new TextField("Message");

        Button send = new Button("Send", e -> {
            if (webSocket != null && webSocket.isOpen()) {
                JsonObject msg = new JreJsonFactory().createObject();
                msg.put("sender", sender.getValue());
                msg.put("content", content.getValue());
                msg.put("type", "TEXT");
                webSocket.send(msg.toJson());
                content.clear();
            } else {
                Notification.show("WebSocket is not open");
            }
        });

        add(sender, content, send, messageList);
        connectWebSocket();
    }

    private void connectWebSocket() {
        try {
            webSocket = new WebSocket("ws://localhost:8080/chat");
            webSocket.setOnmessage(e -> {
                JsonObject msg = new JreJsonFactory().parse(e.getData().toString());

                String sender = msg.getString("sender");
                String type = msg.getString("type");
                String content = msg.getString("content");
                String attachmentUrl = msg.getString("attachmentUrl");

                ListItem item = new ListItem();

                if ("IMAGE".equals(type)) {
                    Image img = new Image(attachmentUrl, "image");
                    img.setWidth("200px");
                    item.add(new Div(new Text(sender + ":")), img);
                } else if ("FILE".equals(type)) {
                    Anchor fileLink = new Anchor(attachmentUrl, "Скачать файл");
                    fileLink.setTarget("_blank");
                    item.add(new Div(new Text(sender + ": ")), fileLink);
                } else {
                    item.setText("[" + sender + "] " + content);
                }

                messageList.add(item);
            });
        } catch (Exception e) {
            Notification.show("Ошибка WebSocket");
        }
    }
}
