    public void connect() {
        LoadConfig loadConfigFile = new LoadConfig();
        Config conf = loadConfigFile.getConfig();
        ConnectionConfiguration config = new ConnectionConfiguration(conf.getServer(), conf.getport(), conf.getHost());
        con = new XMPPConnection(config);
        try {
            con.connect();
            System.out.println("Connect " + con.getHost());
            con.login(conf.getUserName() + "@" + conf.getHost(), conf.getPassword());
            System.out.println("Logged in as " + con.getUser());
            createRoom(conf.getUser());
        } catch (XMPPException e) {
            System.out.println("Failed to connection " + con.getHost());
        }
    }
