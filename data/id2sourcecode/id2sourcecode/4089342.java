    public boolean startConnection() {
        ConnectionConfiguration conf = new ConnectionConfiguration(set.getServer(), Integer.valueOf(set.getPort()).intValue());
        xmppCon = new XMPPConnection(conf);
        try {
            xmppCon.connect();
            xmppCon.login(set.getLogin(), set.getPassword());
        } catch (XMPPException e) {
            e.printStackTrace();
            return false;
        }
        MessageService mesServ = new MessageService(mw, set);
        PacketTypeFilter mesFil = new PacketTypeFilter(Message.class);
        xmppCon.addPacketListener(mesServ, mesFil);
        IQService iqServ = new IQService(mw, set);
        PacketTypeFilter locFil = new PacketTypeFilter(IQ.class);
        xmppCon.addPacketListener(iqServ, locFil);
        set.setJid(xmppCon.getUser());
        return true;
    }
