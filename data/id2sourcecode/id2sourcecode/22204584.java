    public void connect() throws Exception {
        connection.connect();
        connection.login(account.getLogin(), account.getPassword());
        if (!roster.loadRoster()) {
            loadRosterFromServer();
            roster.saveRoster();
        }
        connection.addPacketListener(JabbraController.getInstance().getPacketListener(), new MessageTypeFilter(Message.Type.chat));
        connection.getRoster().addRosterListener(JabbraController.getInstance().getRosterListener());
    }
