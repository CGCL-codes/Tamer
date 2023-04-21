    @Override
    public void run() {
        try {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(this.propsFilePath));
                this.botName = properties.getProperty("botName");
                this.host = properties.getProperty("host");
                this.port = Integer.parseInt(properties.getProperty("port"));
                this.password = properties.getProperty("password");
                this.user = properties.getProperty("user");
                this.serviceName = properties.getProperty("serviceName");
                this.roomUrl = properties.getProperty("roomUrl");
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean connected = false;
            while (!connected) {
                try {
                    config = new ConnectionConfiguration(this.host, this.port, this.serviceName);
                    connection = new XMPPConnection(config);
                    connection.connect();
                    connection.login(this.user, this.password);
                } catch (Exception iggy) {
                    iggy.printStackTrace();
                }
                connected = true;
            }
            connected = false;
            while (!connected) {
                try {
                    room = new MultiUserChat(connection, this.roomUrl);
                    room.join(this.botName);
                } catch (Exception iggy) {
                    iggy.printStackTrace();
                }
                connected = true;
            }
            t.sleep(500);
            room.addMessageListener(new SpamMessageListener());
            room.addParticipantStatusListener(new SimpleStatusListener());
            checkLongNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                t.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
