    public void addMessage(ChatMessageData messageData) {
        addMessage(messageData.getType(), messageData.getChannel(), messageData.getAuthor(), messageData.getContent(), messageData.hasAllyTag() ? messageData.getAllyTag() : null, messageData.hasAllyTag() ? messageData.getAllyName() : null);
    }
