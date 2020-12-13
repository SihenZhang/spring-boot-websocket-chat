package com.sihenzhang.chat.server;

import com.sihenzhang.chat.pojo.Client;
import com.sihenzhang.chat.pojo.Message;
import com.sihenzhang.chat.pojo.MessageType;
import com.sihenzhang.chat.pojo.User;
import com.sihenzhang.chat.utils.JsonUtils;
import com.sihenzhang.chat.utils.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@ServerEndpoint("/chat")
public class WebSocketServer {
    private static final Map<String, Client> ONLINE_CLIENTS = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        User user = new User("匿名用户", WebSocketUtils.getRemoteAddr(session));
        Client client = new Client(user, session);
        ONLINE_CLIENTS.put(session.getId(), client);
        sendMessageToAll(JsonUtils.toJsonString(new Message(MessageType.ENTER, user, "", getOnlineUsers())));
    }

    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Client client = ONLINE_CLIENTS.get(session.getId());
        Message message = JsonUtils.parseObject(jsonStr, Message.class);
        assert message != null;
        if (message.getType() == MessageType.CHANGE) {
            client.getUser().setName(message.getUser().getName());
        }
        sendMessageToAll(JsonUtils.toJsonString(new Message(message.getType(), client.getUser(), message.getMessage(), getOnlineUsers())));
    }

    @OnClose
    public void onClose(Session session) {
        Client client = ONLINE_CLIENTS.get(session.getId());
        ONLINE_CLIENTS.remove(session.getId());
        sendMessageToAll(JsonUtils.toJsonString(new Message(MessageType.QUIT, client.getUser(), "", getOnlineUsers())));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        ONLINE_CLIENTS.remove(session.getId());
        log.error(error.getMessage(), error);
    }

    private synchronized static List<User> getOnlineUsers() {
        return ONLINE_CLIENTS.values().stream().sorted((a, b) -> {
            if (a.getUser().getName().compareTo(b.getUser().getName()) == 0) {
                return a.getSession().getId().compareTo(b.getSession().getId());
            }
            return a.getUser().getName().compareTo(b.getUser().getName());
        }).map(Client::getUser).collect(Collectors.toList());
    }

    private synchronized static void sendMessageToAll(String message) {
        ONLINE_CLIENTS.forEach((id, client) -> {
            try {
                client.getSession().getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
