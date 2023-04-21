    public Client(String readUrl, String writeUrl) {
        _serverInterface = new ChatServerLocal(readUrl, writeUrl, this);
    }
