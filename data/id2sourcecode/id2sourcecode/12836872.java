    public static void main(String[] args) {
        GtalkTest2 gtalkTest2 = new GtalkTest2();
        ConnectionConfiguration connConfig = new ConnectionConfiguration("127.0.0.1", 5222, "derbysoft.com");
        XMPPConnection connection = new XMPPConnection(connConfig);
        try {
            connection.connect();
            System.out.println("Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            System.out.println("Failed to connect to " + connection.getHost());
            System.exit(1);
        }
        try {
            connection.login("hebig", "hebig");
            System.out.println("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            Roster roster = connection.getRoster();
            PacketFilter myFilter = new PacketFilter() {

                public boolean accept(Packet packet) {
                    return true;
                }
            };
            PacketCollector collector = connection.createPacketCollector(myFilter);
            roster.addRosterListener(new RosterListener() {

                public void entriesAdded(Collection<String> addresses) {
                }

                public void entriesDeleted(Collection<String> addresses) {
                }

                public void entriesUpdated(Collection<String> addresses) {
                }

                public void presenceChanged(Presence presence) {
                }
            });
            while (true) {
                Packet packet = collector.nextResult();
                if (packet instanceof Message) {
                    Message msg = (Message) packet;
                    for (RosterEntry e : gtalkTest2.getOnlineEntries(connection)) {
                        if (StringUtils.isNotBlank(msg.getFrom()) && msg.getFrom().indexOf(e.getUser()) != -1) {
                            continue;
                        }
                        System.out.println("online user:" + e.getUser());
                        Chat chat = connection.getChatManager().createChat(e.getUser(), new MessageListener() {

                            public void processMessage(Chat chat, Message message) {
                            }
                        });
                        chat.sendMessage(msg.getFrom() + ":" + msg.getBody());
                    }
                }
            }
        } catch (XMPPException ex) {
            System.out.println("Failed to log in as " + connection.getUser());
            System.exit(1);
        }
        System.out.println("Press enter to disconnect");
        try {
            System.in.read();
        } catch (IOException ex) {
        }
        connection.disconnect();
    }
