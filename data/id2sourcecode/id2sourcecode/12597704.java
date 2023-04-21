    public ChannelGroup getChannelGroup(final String type) throws SQLException {
        final Connection connection = getDatabaseConnection();
        return PERSISTENT_STORE.getChannelGroup(connection, type);
    }
