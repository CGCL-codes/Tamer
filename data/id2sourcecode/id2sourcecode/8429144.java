    private void processNewMessage(Message message) {
        String[] msg = message.getMessage().split(" ");
        if (msg.length >= 2 && msg[1].equalsIgnoreCase("privmsg")) {
            Message processed = CommandProcessor.parseMessage(message.getMessage());
            sendMessageToChannel(processed.getChannel(), processed);
            return;
        }
        sendMessageToChannel(serverName, message);
    }
