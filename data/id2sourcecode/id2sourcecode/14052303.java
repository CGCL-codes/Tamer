    private void message(String data, Connection con) {
        ServerInformation serverInfo = manager.getSessionFor(con).getServerInformation();
        String[] chanPrefixes = serverInfo.getChannelPrefixes();
        String channelPrefixRegex = "";
        if (chanPrefixes != null) {
            for (String prefix : serverInfo.getChannelPrefixes()) {
                channelPrefixRegex += prefix;
            }
            channelPrefixRegex = "[" + channelPrefixRegex + "]";
        }
        MessageEvent me = IRCEventFactory.privateMsg(data, con, channelPrefixRegex);
        String msg = me.getMessage();
        if (msg.startsWith("")) {
            String ctcpString = msg.substring(0, msg.length() - 1).substring(1);
            if (ctcpString.equals("VERSION")) {
                me.getSession().sayRaw("NOTICE " + me.getNick() + " :\001VERSION " + ConnectionManager.getVersion() + "\001\r\n");
            } else if (ctcpString.equals("PING")) {
                me.getSession().sayRaw("NOTICE " + me.getNick() + " :\001PING \001\r\n");
            }
            me = IRCEventFactory.ctcp(me, ctcpString);
        }
        manager.addToRelayList(me);
    }
