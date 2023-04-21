    void doDestroyConnection() {
        if (getLog().isInfoEnabled()) {
            getLog().info("Received destroy connection message for " + getChannel());
        }
        final IConnection connection = getChannel().getConnection();
        if (connection != null) {
            connection.destroy();
        }
    }
