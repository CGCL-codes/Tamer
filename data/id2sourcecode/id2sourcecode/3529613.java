    @Override
    public ReadableByteChannel getChannel() throws IOException {
        if (canEncode()) {
            return ByteUtils.getChannel(getStream());
        } else {
            return getWrappedRepresentation().getChannel();
        }
    }
