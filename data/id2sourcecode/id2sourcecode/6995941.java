    public AsynchronousFileSystemAnnotations(Configuration configuration, ExifMetadataReader metaDataReader, ExifMetadataWriter exifMetadataWriter, SerialTaskRunner taskRunner, EventBus eventBus) {
        this.configuration = configuration;
        this.metaDataReader = metaDataReader;
        this.exifMetadataWriter = exifMetadataWriter;
        this.taskRunner = taskRunner;
        this.eventBus = eventBus;
        lock();
        this.db = new File(configuration.get("db.home"), "an");
        if (!db.exists()) {
            if (!db.mkdirs()) {
                throw new ImageCollectionException("Could not create image annotation directory: " + db);
            }
        }
        Utils.readAll(new File(db, "UNREADABLE"), unreadables);
        Utils.readAll(new File(db, "UNWRITEABLE"), unwriteables);
    }
