    private static CacheConcurrencyStrategy createCache(String usage, String name, boolean mutable) throws MappingException {
        if (log.isDebugEnabled()) log.debug("cache for: " + name + " usage strategy: " + usage);
        final CacheConcurrencyStrategy ccs;
        if (usage.equals(READ_ONLY)) {
            if (mutable) log.warn("read-only cache configured for mutable: " + name);
            ccs = new ReadOnlyCache();
        } else if (usage.equals(READ_WRITE)) {
            ccs = new ReadWriteCache();
        } else if (usage.equals(NONSTRICT_READ_WRITE)) {
            ccs = new NonstrictReadWriteCache();
        } else if (usage.equals(TRANSACTIONAL)) {
            ccs = new TransactionalCache();
        } else if (usage.equals(SHAREABLE_READ_WRITE)) {
            ccs = new ReadWriteSharableCache();
        } else {
            throw new MappingException("cache usage attribute should be read-write, read-only, nonstrict-read-write or transactional");
        }
        return ccs;
    }
