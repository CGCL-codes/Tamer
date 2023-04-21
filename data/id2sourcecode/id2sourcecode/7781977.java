    public static void main(String[] args) throws XMPPException {
        ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        XMPPConnection connection = new XMPPConnection(connConfig);
        connection.connect();
        connection.login("fcube.server@gmail.com", "");
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
        Chat newChat = connection.getChatManager().createChat("ppjjuu@gmail.com", new MessageListener() {

            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: " + message);
            }
        });
        try {
            Message message = new Message("ppjjuu@gmail.com", Type.chat);
            message.addBody("ko", "Hello. �����ٶ�..");
            newChat.sendMessage(message);
        } catch (XMPPException e) {
            throw new XMPPException(e);
        }
        connection.disconnect();
    }
