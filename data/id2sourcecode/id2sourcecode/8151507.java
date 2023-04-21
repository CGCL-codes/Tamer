    public void doPluginlist(CommandConnection connection, String nick, String type) {
        type = type.toUpperCase();
        if (type.equals("CHANNEL")) {
            Enumeration list = bot.getChannelPlugins();
            while (list.hasMoreElements()) {
                ChannelPlugin plug = (ChannelPlugin) list.nextElement();
                connection.sendReply(nick, "PLUGINLIST " + plug.getIdentifier() + " " + plug.getServer().getServer() + " " + plug.getClass().getName());
            }
            connection.sendReply(nick, "PLUGINLIST done.");
            return;
        } else if (type.equals("SERVER")) {
            Enumeration list = bot.getPlugins();
            while (list.hasMoreElements()) {
                OakPlugin plug = (OakPlugin) list.nextElement();
                connection.sendReply(nick, "PLUGINSLIT " + plug.getIdentifier() + " " + plug.getConnection().getServer() + " " + plug.getClass().getName());
            }
            connection.sendReply(nick, "PLUGINLIST done.");
            return;
        }
        connection.sendReply(nick, "PLUGINLIST /ctcp []A]{ PLUGINLIST [CHANNEL|SERVER]");
    }
