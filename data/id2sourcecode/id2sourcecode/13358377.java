    public String _loadDatabase(URL url) {
        LOGGER.dbg("loading: " + url);
        final DatabaseFactoryInfo df = DatabaseFactoryInfo.getDatabaseFactoryInfo(url);
        if (df == null) {
            LOGGER.logError("No database factory found for " + url);
            return null;
        }
        URLConnection con;
        String header;
        String type;
        try {
            con = url.openConnection();
            header = DatabaseManager.getSharedHeaderName(con, compressed, false);
            type = getDatabaseType(df, url);
            if (loadSharedDatabase(df, con, header)) return type;
        } catch (IOException e) {
            LOGGER.logError(e);
            return null;
        }
        Database db = null;
        try {
            begunLoading(df.toString());
            db = series.getDatabaseManager().loadDatabase(this, df, con);
            if (db instanceof PrivateDatabase) header = getNonSharedHeaderName(header);
            if (db != null) {
                dbHeaderTable.put(type, header);
                synchronized (dbCachedTable) {
                    dbCachedTable.put(type, db);
                }
            }
        } catch (Exception e) {
            Environment.getEnvironment().showErrorMessage("Failed to load " + url + " " + e.getMessage());
            e.printStackTrace();
            LOGGER.logError(e);
        } finally {
            finishedLoading();
            if (db != null) {
                flush(series, db);
                series.getDatabaseManager().shareDatabase(header, db);
            }
        }
        return type;
    }
