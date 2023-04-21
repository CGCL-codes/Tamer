    public Color getColor(ChatEvent event) {
        String key = null;
        if (event.getType() == ChatType.CHANNEL_TELL) {
            key = CHAT_CHAT_EVENT_TYPE_COLOR_APPEND_TO + event.getType() + "-" + event.getChannel() + "-color";
        } else {
            key = getKeyForChatType(event.getType());
        }
        return getColorForKeyWithoutDefault(key);
    }
