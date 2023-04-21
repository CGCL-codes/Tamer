    public boolean onConnect(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
        connection.writeWord("* OK IMAP4rev1 server ready" + AbstractImapCommand.LINE_DELIMITER, "US-ASCII");
        return true;
    }
