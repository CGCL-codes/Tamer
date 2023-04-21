    public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
        final Tracer tracer = baseTracer.entering("getConnection(ConnectionSpec connectionSpec)");
        Connection connection;
        try {
            CCIConnectionSpec cciConnectionSpec = (CCIConnectionSpec) connectionSpec;
            ConnectionRequestInfo connectionRequestInfo = new SPIConnectionRequestInfo(cciConnectionSpec.getUserName(), cciConnectionSpec.getPassword(), cciConnectionSpec.getChannelId());
            connection = (Connection) connectionManager.allocateConnection(managedConnectionFactory, connectionRequestInfo);
        } catch (Throwable throwable) {
            tracer.catched(throwable);
            tracer.throwing(throwable);
            throw new ResourceException(throwable.toString());
        }
        tracer.leaving();
        return connection;
    }
