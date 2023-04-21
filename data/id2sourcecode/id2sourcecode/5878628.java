    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println("Starting IM client");
        GtalkTest gtalkTest = new GtalkTest();
        ConnectionConfiguration connConfig = new ConnectionConfiguration("gmail.com", 5222, "gmail.com");
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            System.out.println("Failed to connect to " + connection.getHost());
            System.exit(1);
        }
        try {
            connection.login("jdkcn.ing", "java@ing");
            System.out.println("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
            gtalkTest.getEntries().addAll(connection.getRoster().getEntries());
        } catch (XMPPException ex) {
            System.out.println("Failed to log in as " + connection.getUser());
            System.exit(1);
        }
        ChatManager chatmanager = connection.getChatManager();
        final List<RosterEntry> entries = gtalkTest.getEntries();
        chatmanager.addChatListener(new ChatManagerListener() {

            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new MessageParrot(entries));
            }
        });
        System.out.println("Press enter to disconnect");
        try {
            System.in.read();
        } catch (IOException ex) {
        }
        connection.disconnect();
    }
