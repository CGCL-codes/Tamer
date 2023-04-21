    public static VirtualStorage createStorage(String domain) {
        if (null == domain) return null;
        if ("gmail.com".equalsIgnoreCase(domain)) {
            StorageReader reader = new GoogleStorageReader();
            Properties prop = new Properties();
            prop.setProperty("mail.smtp.host", "smtp.gmail.com");
            prop.setProperty("mail.smtp.port", "465");
            prop.setProperty("mail.smtp.socketFactory.fallback", Boolean.FALSE.toString());
            prop.setProperty("mail.smtp.socketFactory.class", SMTPWriter.SSL_FACTORY);
            prop.setProperty("mail.smtp.auth", Boolean.TRUE.toString());
            prop.setProperty("mail.smtp.socketFactory.port", "465");
            StorageWriter writer = new SMTPWebWriter(prop, reader);
            VirtualStorage storage = new ReaderWriterVirtualStorage(reader, writer);
            storage.configure(StorageCache.getInstance().getParameters());
            return storage;
        }
        return null;
    }
