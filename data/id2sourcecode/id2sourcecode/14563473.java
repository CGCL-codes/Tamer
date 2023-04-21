    protected IChatClient createClient(String host, int port, String readUrl, String writeUrl) {
        return new Client(host, port, readUrl, writeUrl);
    }
