    public LDAPReadWriteConnectionPool(final LDAPConnectionPool readPool, final LDAPConnectionPool writePool) {
        ensureNotNull(readPool, writePool);
        this.readPool = readPool;
        this.writePool = writePool;
    }
