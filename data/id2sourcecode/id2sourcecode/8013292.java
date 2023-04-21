    public static void writeDB(BabuDBConfig cfg, int ver, String targetDir) throws IOException {
        BabuDBVersionReader reader = null;
        try {
            File trgDir = new File(targetDir);
            trgDir.mkdirs();
            reader = new BabuDBVersionReader(ver, cfg.getProps());
            for (String dbName : reader.getAllDatabases()) {
                File dbDir = new File(trgDir, dbName);
                dbDir.mkdir();
                for (int i = 0; i < reader.getNumIndics(dbName); i++) writeIndexFile(dbName, null, i, new File(dbDir, i + ""), reader);
                String[] snaps = reader.getAllSnapshots(dbName);
                if (snaps.length == 0) continue;
                for (String snapName : snaps) for (int i = 0; i < reader.getNumIndics(dbName); i++) writeIndexFile(dbName, snapName, i, new File(dbDir, i + ""), reader);
            }
        } finally {
            if (reader != null) reader.shutdown();
        }
    }
