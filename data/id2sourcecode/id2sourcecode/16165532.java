    @Override
    public ReadableByteChannel getChannel() throws IOException {
        if (canEncode()) {
            return NioUtils.getChannel(getStream());
        }
        return getWrappedRepresentation().getChannel();
    }
