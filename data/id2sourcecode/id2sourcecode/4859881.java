    public static WriterThread create(Database database, int writeDelay) {
        try {
            WriterThread writer = new WriterThread(database, writeDelay);
            Thread thread = new Thread(writer);
            thread.setName("H2 Log Writer " + database.getShortName());
            thread.setDaemon(true);
            thread.start();
            return writer;
        } catch (AccessControlException e) {
            return null;
        }
    }
