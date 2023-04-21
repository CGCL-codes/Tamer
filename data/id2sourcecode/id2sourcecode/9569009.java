    public Messaging(Settings settings) throws MessagingException {
        super(settings);
        connection = new XMPPConnection(new ConnectionConfiguration(getSetting(Setting.xmpp_host), Integer.parseInt(getSetting(Setting.xmpp_port)), getSetting(Setting.xmpp_service)));
        try {
            connection.connect();
        } catch (XMPPException e) {
            throw new MessagingException(e);
        }
        try {
            connection.login(getSetting(Setting.xmpp_username), getSetting(Setting.xmpp_password), getSetting(Setting.xmpp_resource));
        } catch (XMPPException e) {
            throw new MessagingException(e);
        }
        connection.addPacketListener(new PacketListener() {

            public void processPacket(Packet packet) {
                org.jivesoftware.smack.packet.Message rawMessage = (org.jivesoftware.smack.packet.Message) packet;
                Message message = new Message();
                message.setFrom(rawMessage.getFrom());
                message.setPayload(rawMessage.getBody());
                message.setTo(rawMessage.getTo());
                MessageEvent messageEvent = new MessageEvent(this, message);
                fireMessageEvent(messageEvent);
            }
        }, new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class));
    }
