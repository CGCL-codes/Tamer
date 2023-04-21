    public static void main(String[] args) throws Exception {
        final Object lock = new Object();
        final int numDBs = 5;
        final int numIndices = 1;
        final int numThreads = 1;
        final int maxLogFileSize = 1024 * 1024 * 16;
        final String dbDir = "c:/temp/dbtest";
        final int minKeySize = 5;
        final int maxKeySize = 40;
        final int minValSize = 5;
        final int maxValSize = 16384;
        final BabuDB databaseSystem = BabuDBFactory.createBabuDB(new BabuDBConfig(dbDir, dbDir, numThreads, maxLogFileSize, 10, SyncMode.ASYNC, 1000, 0, false, 16, 1024 * 1024 * 256));
        DatabaseManager dbm = databaseSystem.getDatabaseManager();
        dbm.createDatabase("blub", numIndices);
        final Database[] dbs = new Database[numDBs];
        for (int i = 0; i < dbs.length; i++) dbs[i] = dbm.getDatabase("DB" + i);
        Thread writeThread = new Thread() {

            public void run() {
                try {
                    System.out.println("starting write thread...");
                    for (int i = 0; i < NUM_INSERTS; i++) {
                        int db = (int) (Math.random() * numDBs);
                        byte[] key = randomBytes(minKeySize, maxKeySize);
                        byte[] val = randomBytes(minValSize, maxValSize);
                        int index = (int) (Math.random() * numIndices);
                        dbs[db].singleInsert(index, key, val, null).get();
                        if (i % 1000 == 0) {
                            System.out.print(".");
                            Thread.sleep(5000);
                        }
                        if (i % 10000 == 9999) {
                            System.out.println("\ncheckpoint...");
                            databaseSystem.getCheckpointer().checkpoint();
                            System.out.println("done");
                        }
                        if (i % 40000 == 39999) System.exit(0);
                    }
                    System.out.println("write thread finished");
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        };
        Thread readThread = new Thread() {
        };
        writeThread.start();
        readThread.start();
        writeThread.join();
        readThread.join();
        databaseSystem.shutdown();
    }
