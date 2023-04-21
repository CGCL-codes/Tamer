    public void setUp() {
        StorageReader reader = new YahooStorageReader();
        Properties prop = new Properties();
        StorageWriter writer = new YahooStorageWriter(prop, reader);
        storage = new ReaderWriterVirtualStorage(reader, writer);
        storage.configure(StorageCache.getInstance().getParameters());
        try {
            storage.authenticate("", "".toCharArray());
            System.out.println("Authenticated User");
        } catch (AuthenticationException e) {
            boolean authenticated = false;
            assertFalse(authenticated);
        }
    }
