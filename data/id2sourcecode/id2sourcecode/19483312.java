    public Connection createConnection(TcpConnectionContext aConnectionContext) throws ConnectionException {
        try {
            mConfigurator.configureSocket(aConnectionContext.getChannel().socket());
            return new TcpConnection(aConnectionContext, mDecoderFactory, mEncoderFactory);
        } catch (IOException e) {
            if (e instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            throw new ConnectionException(e);
        }
    }
