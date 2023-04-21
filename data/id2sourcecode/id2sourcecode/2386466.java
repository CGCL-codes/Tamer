    public static Db getMainDbRW() throws Exception {
        _logger.debug("Returning pooled read/write db object for main database.");
        return poolMainRW.getDb();
    }
