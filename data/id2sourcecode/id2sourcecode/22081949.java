    public void handleConsoleMsg(IRCMessage message) {
        int i;
        StringBuffer buffer;
        IRCLine line;
        int index;
        line = message.getIRCLine();
        switch(message.getMsgType()) {
            case IRCMessage.MSG_JOIN:
                IRCPanel panel = null;
                IRCPanel window;
                String channel;
                IRCConnection connection;
                boolean foundPanel;
                System.out.println("Client::handleConsoleMsg called with JOIN");
                foundPanel = false;
                channel = message.getTarget();
                connection = message.getConnection();
                for (i = 0; i < panels.size(); i++) {
                    panel = (IRCPanel) panels.elementAt(i);
                    if (panel.getChannelName() != null && panel.getChannelName().equalsIgnoreCase(channel)) {
                        foundPanel = true;
                        break;
                    }
                }
                if (!foundPanel) {
                    window = console;
                    if (window.isTaken()) panel = window.getParentWindow().createPanel(channel, id); else {
                        panel = window;
                    }
                    foundPanel = true;
                }
                if (foundPanel) {
                    connection.registerChannelListener(channel, panel);
                    panel.setChannelName(channel);
                }
                break;
            case IRCMessage.MSG_NOTICE:
                console.showString("--- " + message.getIRCLine().getRemaining());
                break;
            default:
                buffer = new StringBuffer();
                buffer.append(message.getType() + " ");
                for (i = 0; i < message.getParamCount(); i++) buffer.append((String) message.getParam(i) + " ");
                buffer.append(message.getIRCLine().getRemaining());
                if (console == null) System.out.println("message to non-existant console" + buffer.toString()); else console.showString(buffer.toString());
        }
    }
