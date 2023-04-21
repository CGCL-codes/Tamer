    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, XMPPException, InterruptedException, ParserConfigurationException, SAXException {
        System.out.println("Mibew Jabber transport application");
        Parameters p = new Parameters(args);
        if (!p.load()) {
            return;
        }
        XMPPConnection connection = new XMPPConnection(p.fJabberServer);
        connection.connect();
        connection.login(p.fJabberLogin, p.fJabberPassword);
        final Chat chat = connection.getChatManager().createChat(p.fJabberAdmin, new MessageListener() {

            public void processMessage(Chat chat, Message message) {
                System.out.println("Received message: " + message.getThread() + " " + message.getBody());
            }
        });
        MibewConnection conn = new MibewConnection("http://localhost:8080/webim/", "admin", "1");
        if (!conn.connect()) {
            System.err.println("Wrong server, login or password.");
            return;
        }
        MibewTracker mt = new MibewTracker(conn, new MibewTrackerListener() {

            @Override
            public void threadCreated(MibewThread thread) {
                try {
                    chat.sendMessage(thread.getId() + ": " + thread.getAddress() + " " + thread.getClientName());
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        });
        connection.disconnect();
    }
