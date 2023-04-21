    public void login() {
        try {
            conn.connect();
            conn.login(data.Username, data.Password, "Lima-City Android App");
            DiscussionHistory history = new DiscussionHistory();
            history.setMaxStanzas(5);
            muc = new MultiUserChat(conn, "support@conference.jabber.lima-city.de");
            muc.addMessageListener(new PacketListener() {

                @Override
                public void processPacket(Packet message) {
                    onMessageReceived(message);
                }
            });
            muc.join(data.Username, data.Password, history, SmackConfiguration.getPacketReplyTimeout());
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
