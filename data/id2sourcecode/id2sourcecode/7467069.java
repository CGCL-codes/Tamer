    public void receive(String folderName) throws PopReceiveException {
        LOG.trace("ENTER : " + hostUsername + "@" + hostAddress + ":" + hostPort);
        Store store = getStore();
        Folder folder = null;
        try {
            LOG.trace("Connecting to email store: " + hostAddress + ":" + hostPort);
            store.connect();
            folder = store.getDefaultFolder().getFolder(folderName);
            if (folder == null) throw new MessagingException("Got null handle for requested folder: '" + folderName + "'");
            try {
                LOG.trace("Attempting to open folder for read/write.");
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException ex) {
                LOG.trace("Opening folder for Read/write failed.  Attempting to open folder for read only.");
                folder.open(Folder.READ_ONLY);
            }
            Message[] messages = folder.getMessages();
            for (Message message : messages) {
                if (emailFilter == null || emailFilter.accept(message)) {
                    if (emailFilter != null) LOG.info("Email accepted by filter.  Beginning processing.");
                    processor.processPopMessage(message);
                } else {
                    LOG.info("Email rejected by filter.");
                }
            }
            LOG.trace("EXIT : POP email account checked without error.");
        } catch (MessagingException ex) {
            LOG.error("Unable to connect to POP account.", ex);
            throw new PopReceiveException(ex);
        } finally {
            if (folder != null) try {
                folder.close(true);
            } catch (MessagingException ex) {
                LOG.warn("Error closing POP folder.", ex);
            }
            try {
                store.close();
            } catch (MessagingException ex) {
                LOG.warn("Error closing POP store.", ex);
            }
        }
    }
