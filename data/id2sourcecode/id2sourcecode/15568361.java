    protected boolean setAdd(CharSequence uri) {
        DatabaseEntry key = new DatabaseEntry();
        LongBinding.longToEntry(createKey(uri), key);
        long started = 0;
        OperationStatus status = null;
        try {
            if (logger.isLoggable(Level.INFO)) {
                started = System.currentTimeMillis();
            }
            status = alreadySeen.putNoOverwrite(null, key, ZERO_LENGTH_ENTRY);
            if (logger.isLoggable(Level.INFO)) {
                aggregatedLookupTime += (System.currentTimeMillis() - started);
            }
        } catch (DatabaseException e) {
            logger.severe(e.getMessage());
        }
        if (status == OperationStatus.SUCCESS) {
            count++;
            if (logger.isLoggable(Level.INFO)) {
                final int logAt = 10000;
                if (count > 0 && ((count % logAt) == 0)) {
                    logger.info("Average lookup " + (aggregatedLookupTime / logAt) + "ms.");
                    aggregatedLookupTime = 0;
                }
            }
        }
        if (status == OperationStatus.KEYEXIST) {
            return false;
        } else {
            return true;
        }
    }
