    private Folder openFolder(boolean readwrite) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Debug.debug("got session", 4);
        Store store = session.getStore(getServerType());
        store.connect(getServer(), getUserName(), getPassword());
        Debug.debug("got store", 4);
        Folder folder = store.getFolder(getFolder());
        if (readwrite) {
            folder.open(Folder.READ_WRITE);
        } else {
            folder.open(Folder.READ_ONLY);
        }
        return folder;
    }
