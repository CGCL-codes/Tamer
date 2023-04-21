    private WriterThread(Database database, int writeDelay) {
        this.databaseRef = new WeakReference<Database>(database);
        this.writeDelay = writeDelay;
    }
