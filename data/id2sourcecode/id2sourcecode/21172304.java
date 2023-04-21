    private IMClient() throws Exception {
        imUser = new IMUser();
        try {
            conn1 = new GoogleTalkConnection();
            conn1.connect();
        } catch (Exception e) {
            throw new Exception("Failed to connect to server");
        }
        try {
            conn1.login(imUser.getUsername(), imUser.getPassword());
        } catch (Exception e) {
            throw new Exception("Failed to login as " + imUser.getUsername());
        }
        Roster roster = conn1.getRoster();
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            System.out.println(entry + " " + entry.getStatus());
        }
        try {
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            PacketListener myListener = new MyPacketListener();
            conn1.addPacketListener(myListener, filter);
        } catch (Exception e) {
            throw new Exception("Failed to add packetListener");
        }
    }
