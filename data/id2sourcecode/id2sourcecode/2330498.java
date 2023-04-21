    public void run() {
        Logger logger = Logger.getInstance();
        logger.log(Logger.INFO, "Starting napster server (port=" + port + ",maxConnections=" + maxConnections);
        int sessionTimeout = getJNerveConfiguration().getSessionTimeout() * 1000;
        try {
            ServerSocket socket = new ServerSocket(port, maxConnections);
            while (true) {
                Socket estSocket = socket.accept();
                estSocket.setTcpNoDelay(true);
                estSocket.setSoTimeout(sessionTimeout);
                Session s = new Session(estSocket, this);
                s.addSessionEventListener(getHotlist());
                s.addSessionEventListener(getChannelManager());
                s.addSessionEventListener(this);
                s.start();
            }
        } catch (IOException ioe) {
            logger.log(Logger.SEVERE, ioe.toString());
        }
    }
