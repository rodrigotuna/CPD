package server.handler;

import server.message.JoinMessage;

public class JoinMessageHandler {
    private final JoinMessage joinMessage;

    public JoinMessageHandler(JoinMessage joinMessage) {
        this.joinMessage = joinMessage;
    }
}
