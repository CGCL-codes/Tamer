    public static void main(String[] args) throws Exception {
        String username = "nsano";
        String password = "12345963";
        String server = "jabber.jp";
        XMPPConnection connection = new XMPPConnection(server);
        connection.connect();
        connection.login(username, password, "SomeResource");
        ChatManager chatmanager = connection.getChatManager();
        Chat chat = chatmanager.createChat("sano-n@jabber.jp", new MessageListener() {

            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: " + message);
            }
        });
        chat.sendMessage("Hello!");
        connection.disconnect();
    }
