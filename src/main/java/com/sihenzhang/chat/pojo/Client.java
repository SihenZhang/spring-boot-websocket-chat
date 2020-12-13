package com.sihenzhang.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private User user;
    private Session session;

    public Client(String username, String ipAddress, Session session) {
        this(new User(username, ipAddress), session);
    }
}
