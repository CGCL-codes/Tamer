    @Test
    public void connect() {
        try {
            ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
            config.setDebuggerEnabled(true);
            XMPPConnection con = new XMPPConnection(config);
            con.connect();
            con.login("simconomy", "qwerty123");
            String test = "";
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
