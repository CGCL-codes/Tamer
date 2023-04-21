    public synchronized void sendJoin(Channel chan) {
        if (_connection.getState() != IRCConnection.CONNECTED) {
            fireStatusEvent("Ignoring JOIN command, not connected.");
            return;
        }
        if (!getChannels().contains(chan.getName())) {
            addChannel(chan);
            sendCommand("JOIN " + chan.getName());
        } else {
            fireStatusEvent("Ignoring JOIN command, already in channel.");
            return;
        }
    }
