    public void connect() throws Exception {
        client.connect(hostName, 110);
        client.login(userName, password);
    }
