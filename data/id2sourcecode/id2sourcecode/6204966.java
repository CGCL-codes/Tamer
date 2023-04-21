    public DatabaseConnection getReadWriteConnection() throws SQLException {
        DatabaseConnection conn = getSavedConnection();
        if (conn != null) {
            return conn;
        }
        if (connection == null) {
            SQLiteDatabase db;
            if (sqliteDatabase == null) {
                try {
                    db = helper.getWritableDatabase();
                } catch (android.database.SQLException e) {
                    throw SqlExceptionUtil.create("Getting a writable database from helper " + helper + " failed", e);
                }
            } else {
                db = sqliteDatabase;
            }
            connection = new AndroidDatabaseConnection(db, true);
            logger.trace("created connection {} for db {}, helper {}", connection, db, helper);
        } else {
            logger.trace("{}: returning read-write connection {}, helper {}", this, connection, helper);
        }
        return connection;
    }
