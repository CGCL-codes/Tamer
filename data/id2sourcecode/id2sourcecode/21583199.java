    public void onPublicMessage(String nickname, String host, String username, String message, String channel, be.trc.core.IRCServer server) {
        try {
            if (iThread.server == null) iThread.server = server;
            if (oThread.server == null) oThread.server = server;
            message = message.replaceAll("\003[0-9]{0,2}(\\,[0-9]{1,2})?", "").replaceAll("\003|\002", "");
            message = message.trim();
            zorobot.message(getChannelId(channel), msgId++, nickname, new Message(message));
        } catch (Exception e) {
            ZorobotSystem.exception(e);
        }
    }
