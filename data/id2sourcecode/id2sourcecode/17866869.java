    public static void main(String[] args) {
        try {
            JabberAccountManager accountManager = new JabberAccountManager();
            final String server = "www2.ofai.at";
            final String id = "rascalli-foobar-1";
            final String pwd = "foobar";
            accountManager.registerAccount(server, id, pwd);
            XMPPConnection connection = new XMPPConnection(server);
            connection.connect();
            connection.login(id, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
