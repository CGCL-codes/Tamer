    protected boolean isJoined(ChatRoomIrcImpl chatRoom) {
        if (ircMUCOpSet.findSystemRoom().equals(chatRoom)) return isConnected();
        if (chatRoom.isPrivate()) return true;
        if (this.isConnected()) {
            String[] channels = this.getChannels();
            for (int i = 0; i < channels.length; i++) {
                if (chatRoom.getName().equals(channels[i])) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
