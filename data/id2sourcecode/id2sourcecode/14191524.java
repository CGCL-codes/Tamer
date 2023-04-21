    public void clearIrcWindows(boolean clearChannelCache) {
        ircChatLogText.setText("");
        ircUsersList.removeAll();
        if (clearChannelCache) {
            Vector list = ircMgr.getChannels();
            for (int i = 0; i < list.size(); i++) ((IrcChannel) list.elementAt(i)).clearAll();
        }
    }
