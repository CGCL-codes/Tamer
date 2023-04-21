    public void executeLineTyped(String line) {
        if (line.trim().equals("")) return;
        if (!isCommand(line)) {
            if (currentChannel == null) {
                System.out.println("[Error] you are not member of any channels ...");
                return;
            }
            line = "/msg " + currentChannel + " " + line;
        }
        if (listener != null) {
            IRCMessage msg = listener.constructMessage(line);
            if (!listener.isValidCommand(msg)) return;
            if (msg.getCommand().equals("CONNECT")) {
                listener.connect();
                listener.login();
            } else if (msg.getCommand().equals("DISCONNECT")) {
                IRCMessage bePolite = new IRCMessage("QUIT", new String[] {});
                listener.sendCommand(bePolite);
                listener.disconnect();
                System.exit(0);
            } else {
                listener.sendCommand(msg);
            }
        }
    }
