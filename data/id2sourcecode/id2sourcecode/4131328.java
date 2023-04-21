    public static void sendXMPP(String fromUserId, String fromUserPw, String toUserId, String text) {
        log.info("---------------------------------------");
        log.info("Try to send Google Talk chat message...");
        log.info("from:" + fromUserId);
        log.info("pass:" + fromUserPw);
        log.info("to  :" + toUserId);
        log.info("text:" + text);
        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
        XMPPConnection connection = new XMPPConnection(config);
        try {
            connection.connect();
            connection.login(fromUserId, fromUserPw);
        } catch (XMPPException ex) {
            ex.printStackTrace();
        }
        ChatManager chatmanager = connection.getChatManager();
        org.jivesoftware.smack.Chat newChat = chatmanager.createChat(toUserId, new MessageListener() {

            public void processMessage(org.jivesoftware.smack.Chat chat, org.jivesoftware.smack.packet.Message message) {
                System.out.println("Received message: " + message);
            }
        });
        try {
            newChat.sendMessage(text);
            log.info("XMPP message sent OK.");
        } catch (XMPPException e) {
            System.out.println("ERROR Delivering block");
        }
    }
