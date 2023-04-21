    @Override
    public InputStream getStream() throws IOException {
        return ByteUtils.getStream(getChannel());
    }
