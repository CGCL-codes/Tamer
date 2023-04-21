    public void statusChanged(IRCConnection conn, int oldStatus) {
        if (conn.getStatus() == Session.AUTHENTICATING) {
            String args[];
            IRCMessage msg;
            if (!getPassword().equals("")) conn.send(PassMessage.createMessage("", "", "", getPassword()));
            conn.send(NickMessage.createMessage("", "", "", createNick()));
            conn.send(UserMessage.createMessage("", "", "", userName, "0", realName));
        } else if (conn.getStatus() == Session.CONNECTED) {
            AbstractIRCChannel[] chans = getChannels();
            IRCMessage msg;
            for (int i = 0; i < chans.length; i++) {
                if (IRCUtils.isChannel(chans[i].getName())) {
                    msg = JoinMessage.createMessage("", "", "", chans[i].getName());
                    conn.send(msg);
                }
            }
        } else if (conn.getStatus() == Session.UNCONNECTED) {
            AbstractIRCChannel[] chans = getChannels();
            for (int i = 0; i < chans.length; i++) removeChannel(chans[i].getName());
        }
        fireStatusChangedEvent(this, oldStatus);
        if (reconnect) reconnect();
    }
