    private Event getDefaultEvent(String textReply) {
        String[] parts = textReply.split(" :");
        String channelName = host;
        String userName = null;
        String[] temp = parts[0].split(" ");
        if (parts[0].contains("PRIVMSG")) {
            channelName = temp[2];
            userName = temp[0].substring(1, temp[0].indexOf('!'));
            textReply = String.format("%s: %s", userName, parts[1]);
        }
        Room room = state.getChannel(channelName);
        room.updateText(textReply);
        return new UpdateChatEvent(channelName, textReply);
    }
