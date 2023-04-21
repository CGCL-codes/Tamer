    public synchronized void sendJoin(String name) {
        if (_connection.getState() != IRCConnection.CONNECTED) {
            fireStatusEvent("Ignoring JOIN command, not connected.");
            return;
        }
        name = name.trim().toLowerCase();
        Channel chan = (Channel) getChannels().get(name);
        if (chan == null) {
            sendCommand("JOIN " + name);
        } else {
            fireStatusEvent("Ignoring JOIN command, already in channel.");
        }
    }
