    @Override
    public InputStream getStream() throws IOException {
        return NioUtils.getStream(getChannel());
    }
