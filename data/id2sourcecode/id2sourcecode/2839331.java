    public void markReadWrite(String entityTag) {
        if (_logger.isDebugEnabled()) {
            _logger.debug("Marking txn entity tag '" + entityTag + "' read/write");
        }
        _modifiedEntities.put(entityTag, entityTag);
    }
