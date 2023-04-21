    @Override
    public ReadableByteChannel getChannel() throws IOException {
        if (isDecoding()) {
            return NioUtils.getChannel(getStream());
        }
        return getWrappedRepresentation().getChannel();
    }
