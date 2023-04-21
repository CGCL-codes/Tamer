    public void connect() throws XMPPException {
        connection.connect();
        collector = connection.createPacketCollector(filter);
        connection.login(id, password);
        final Presence p = new Presence(Presence.Type.available);
        p.setStatus("A stalwart of the grabbing industry since " + new Date());
        connection.sendPacket(p);
        connection.getRoster().addRosterListener(rl);
        System.out.println("Connected at " + new Date());
    }
