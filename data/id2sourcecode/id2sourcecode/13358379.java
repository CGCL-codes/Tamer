    DatabaseFactoryInfo autoLoadDatabase(URL url, UDataInputStream in) throws IOException {
        DatabaseFactoryInfo df = DatabaseFactoryInfo.getDatabaseFactoryInfo(url);
        URLConnection con = url.openConnection();
        String header = DatabaseManager.getSharedHeaderName(con, compressed, false);
        String type = getDatabaseType(df, url);
        if (loadSharedDatabase(df, con, header)) return df;
        Database db = null;
        try {
            begunLoading(df.toString());
            db = df.loadDatabase(this, in);
            dbHeaderTable.put(type, header);
            synchronized (dbCachedTable) {
                dbCachedTable.put(type, db);
            }
        } finally {
            finishedLoading();
            if (db != null) {
                flush(series, db);
                series.getDatabaseManager().shareDatabase(header, db);
            }
        }
        return df;
    }
