    protected IChatClient createClient(String readUrl, String writeUrl) {
        return new Client(readUrl, writeUrl);
    }
