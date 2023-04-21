    public Channel getChannel(Connection connection) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(connection.getUserName(), connection.getHost(), connection.getPort());
        session.setPassword(connection.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        Channel channel = session.openChannel(connection.getProtocol());
        channel.connect();
        return channel;
    }
