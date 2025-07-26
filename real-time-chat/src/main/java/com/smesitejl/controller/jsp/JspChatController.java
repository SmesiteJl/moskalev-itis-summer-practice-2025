package com.smesitejl.controller.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JspChatController {

    @GetMapping("/jsp-chat")
    public String chatPage() {
        return "views/chat";
    }
}
