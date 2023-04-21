    public static Db npMainDbRW() throws Exception {
        _logger.debug("Returning non-pooled read/write db object for main database.");
        return new Db(new DbAuth(null, StaticConfig.mainDbUsername, StaticConfig.mainDbPassword, StaticConfig.mainDbConnection, 0), null, oldDatabaseConnection(), false, true);
    }
