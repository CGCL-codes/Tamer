    public ChatServerLocal(String readUrl, String writeUrl, IChatClient responseInterface) {
        _readUrl = readUrl;
        _writeUrl = writeUrl;
        _responseInterface = responseInterface;
        _attemptToTunnel = true;
        _alwaysTunnel = true;
    }
