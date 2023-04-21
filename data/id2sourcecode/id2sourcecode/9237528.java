    public ChatServerLocal(String host, int port, String readUrl, String writeUrl, IChatClient responseInterface) {
        _readUrl = readUrl;
        _writeUrl = writeUrl;
        _host = host;
        _port = port;
        _responseInterface = responseInterface;
        _socketOpener = new SocketOpener(host, port);
        _attemptToTunnel = true;
    }
