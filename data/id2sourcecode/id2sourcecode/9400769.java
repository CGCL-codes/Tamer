    private Folder connect4readwrite() throws MessagingException {
        Folder inbox = this.connect();
        inbox.open(READ_WRITE);
        return inbox;
    }
