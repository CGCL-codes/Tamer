    private SocketChannel configureChannel() throws IOException {
        namedChannel.getChannel().configureBlocking(true);
        LoggerUtils.fine(logger, feederManager.getEnvImpl(), "Log File Feeder accepted connection from " + namedChannel);
        namedChannel.getChannel().socket().setSoTimeout(SOCKET_TIMEOUT_MS);
        namedChannel.getChannel().socket().setTcpNoDelay(false);
        return namedChannel.getChannel();
    }
