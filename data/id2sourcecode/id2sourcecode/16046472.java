    public AndroidDatabaseConnection(SQLiteDatabase db, boolean readWrite) {
        this.db = db;
        this.readWrite = readWrite;
        logger.trace("{}: db {} opened, read-write = {}", this, db, readWrite);
    }
