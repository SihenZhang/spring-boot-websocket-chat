package com.sihenzhang.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private MessageType type;
    private User user;
    private String message;
    private List<User> onlineUsers;
}
