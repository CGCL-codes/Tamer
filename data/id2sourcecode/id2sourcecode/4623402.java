    public Client(String host, int port, String readUrl, String writeUrl) {
        _serverInterface = new ChatServerLocal(host, port, readUrl, writeUrl, this);
    }
