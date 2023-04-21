    public void connect(String host, int port, Properties props) throws IOException {
        this.screenName = props.getProperty("username");
        String password = props.getProperty("password");
        this.props = props;
        connectToHost(host, port, Status.CONNECTING);
        OscarEventHandler eventHandler = new OscarEventHandler(this);
        eventHandler.setIMListeners(listeners);
        eventHandler.start();
        writer = new WriterThread(connection, writeBuffer);
        writer.start();
        sendToWriterThread(new LoginCommand(screenName, password));
    }
