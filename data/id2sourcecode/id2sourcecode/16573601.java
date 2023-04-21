    @Override
    public java.nio.channels.ReadableByteChannel getChannel() throws IOException {
        return org.restlet.engine.io.NioUtils.getChannel(getStream());
    }
