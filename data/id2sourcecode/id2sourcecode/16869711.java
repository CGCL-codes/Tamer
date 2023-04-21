    public LDAPReadWriteConnectionPool(final LDAPConnection readConnection, final int initialReadConnections, final int maxReadConnections, final LDAPConnection writeConnection, final int initialWriteConnections, final int maxWriteConnections) throws LDAPException {
        ensureNotNull(readConnection, writeConnection);
        ensureTrue(initialReadConnections >= 1, "LDAPReadWriteConnectionPool.initialReadConnections must be " + "at least 1.");
        ensureTrue(maxReadConnections >= initialReadConnections, "LDAPReadWriteConnectionPool.initialReadConnections must not " + "be greater than maxReadConnections.");
        ensureTrue(initialWriteConnections >= 1, "LDAPReadWriteConnectionPool.initialWriteConnections must be " + "at least 1.");
        ensureTrue(maxWriteConnections >= initialWriteConnections, "LDAPReadWriteConnectionPool.initialWriteConnections must not " + "be greater than maxWriteConnections.");
        readPool = new LDAPConnectionPool(readConnection, initialReadConnections, maxReadConnections);
        try {
            writePool = new LDAPConnectionPool(writeConnection, initialWriteConnections, maxWriteConnections);
        } catch (LDAPException le) {
            debugException(le);
            readPool.close();
            throw le;
        }
    }
