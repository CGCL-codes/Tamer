    public void sendStatusUpdate() throws Exception {
        if (!isConnected()) {
            connect();
            return;
        }
        BNetUser user = master.getMyUser();
        String channel = master.getChannel();
        int ip = -1;
        if (channel == null) channel = "<Not Logged On>"; else ip = master.getIp();
        if ((myUser != null) && (myUser instanceof BotNetUser)) {
            BotNetUser me = (BotNetUser) myUser;
            me.name = GlobalSettings.botNetUsername;
            if ((me.name == null) || (me.name.length() == 0)) me.name = "BNUBot2";
            if (user != null) me.name = user.getShortLogonName();
            me.channel = channel;
            me.server = ip;
            me.database = GlobalSettings.botNetDatabase;
            dispatchBotnetUserStatus(me);
        }
        sendStatusUpdate((user == null) ? "BNUBot2" : user.getShortLogonName(), channel, ip, GlobalSettings.botNetDatabase + " " + GlobalSettings.botNetDatabasePassword, false);
    }
