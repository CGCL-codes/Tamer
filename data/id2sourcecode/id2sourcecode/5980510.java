    public void login(String user, String passwd) throws XMPPException {
        myUserName = user;
        if (!myUserName.contains("@gmail.com")) {
            myUserName += "@gmail.com";
        }
        connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        connection = new XMPPConnection(connConfig);
        connection.connect();
        connection.login(myUserName, passwd);
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        connection.addPacketListener((PacketListener) this, filter);
        this.start();
    }
