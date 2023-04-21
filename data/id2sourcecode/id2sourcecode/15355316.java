    public void test() {
        String[] args = {};
        System.out.println("Starting IM client");
        ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            System.out.println("Failed to connect to " + connection.getHost());
            System.exit(1);
        }
        try {
            connection.login(username, password);
            System.out.println("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
        } catch (XMPPException ex) {
            System.out.println("Failed to log in as " + username);
            System.exit(1);
        }
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        connection.addPacketListener(new MessageParrot(connection), filter);
        if (args.length > 0) {
            Message msg = new Message(args[0], Message.Type.chat);
            msg.setBody("Test");
            connection.sendPacket(msg);
        }
        System.out.println("Press enter to disconnect\n");
        try {
            System.in.read();
        } catch (IOException ex) {
        }
        connection.disconnect();
    }
