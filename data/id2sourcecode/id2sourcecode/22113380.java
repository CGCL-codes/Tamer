    public FileSystemAnnotations(Configuration configuration, ExifMetadataReader metadataReader, ExifMetadataWriter metadataWriter, EventBus eventBus, FsUtils fsUtils) {
        this.configuration = configuration;
        this.metaDataReader = metadataReader;
        this.exifMetadataWriter = metadataWriter;
        this.eventBus = eventBus;
        this.fsUtils = fsUtils;
        this.db = new File(configuration.get("db.home"), "db");
        if (!db.exists()) {
            if (!db.mkdirs()) {
                throw new ImageCollectionException("Could not create image annotation directory: " + db);
            }
        }
        Utils.readAll(new File(db, "UNREADABLE"), unreadables);
        Utils.readAll(new File(db, "UNWRITEABLE"), unwriteables);
    }
