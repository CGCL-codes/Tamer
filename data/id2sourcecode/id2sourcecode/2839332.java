    public boolean isReadWrite(String entityTag) {
        boolean isReadWrite = _modifiedEntities.containsKey(entityTag);
        if (_logger.isDebugEnabled()) {
            _logger.debug("Checking txn entity tag '" + entityTag + "' for read/write access. Returning: " + isReadWrite);
        }
        return isReadWrite;
    }
