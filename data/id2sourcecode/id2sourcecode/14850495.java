    public void connect(boolean fork) {
        this.connectionStatus = BEGIN_CONNECT;
        if (fork) {
            Thread connect = new Thread("connect") {

                public void run() {
                    Client.getInstance().connect(false);
                }
            };
            connect.start();
        } else {
            System.out.println("connecting...");
            if (this.isConnected()) {
                return;
            }
            io.reset();
            try {
                Socket s = new Socket();
                s.setSoTimeout(1000);
                System.out.println("Establishing Connection");
                s.connect(new InetSocketAddress(Client.ip, Client.port), 2000);
                System.out.println("Connection Established");
                s.setSoTimeout(15000);
                s.setKeepAlive(true);
                io.initIO(s);
            } catch (IOException e) {
                e.printStackTrace();
                this.connectionStatus = DISCONNECTED;
                this.setError(ClientError.CONNECT_SERVER_TIMEOUT, "connect failure: server timeout");
                this.clientview.updateStatusTS();
            }
            while (!this.io.hasNextMessage(MessageFactory.LOGIN)) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.md5 = this.io.getNextMessage(MessageFactory.LOGIN);
            this.login(false);
        }
    }
