    public void processLine(String tt) {
        textin.setText("");
        if (tt == null || tt.equals("") || tt.trim().equals("")) return;
        if (tt.charAt(0) != '/') {
            if (currentChannel == null) {
                textout.append("[BasicGUI] Commands must start with / !\n");
                return;
            } else {
                System.out.println("Sending channel message");
                tt = "/PRIVMSG " + currentChannel.toString() + " " + tt;
            }
        }
        if (listener != null) {
            IRCMessage p = listener.constructMessage(tt);
            if (p.getCommand().equals(OJMessageProcessor.FAKEMESSAGECOMMAND)) {
                textout.append("[OJIRC] Invalid command");
                return;
            }
            if (p.getCommand().equals(OJConstants.ID_CLIENT_PRIVMSG)) {
                ChatChannel x = findChannel(p.getParameter(0));
                if (x != null) {
                    addTextToChannel(constructPrivateMessage(listener.getParameter("nickname"), p.getJoinedParameter(1).substring(1)), x.channelName);
                } else {
                    addTextToChat(constructPrivateMessage(listener.getParameter("nickname") + "->" + p.getParameter(0), p.getJoinedParameter(1).substring(1)));
                }
            }
            String com = p.getCommand();
            if (com.equals("CONNECT")) {
                listener.connect();
                listener.login();
            } else if (com.equals("DISCONNECT")) {
                listener.sendCommand(new IRCMessage("QUIT"));
                textout.append("Trying to disconnect...");
                try {
                    Thread.currentThread().sleep(1000);
                    listener.disconnect();
                    System.exit(0);
                } catch (Exception e) {
                    System.exit(1);
                } finally {
                    textout.append("Disconnected.");
                }
            } else {
                listener.sendCommand(p);
            }
        }
    }
