    public void join(ChatAvatar chatAvatar) {
        if (chatAvatar != null && !myChatAvatars.contains(chatAvatar)) {
            myChatAvatars.add(chatAvatar);
            onChatEvent(new ChatEventImpl(chatAvatar.getNickname() + " joined channel " + getChannelIdentifier() + "."));
        }
    }
