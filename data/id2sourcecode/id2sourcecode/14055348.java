    @Override
    public ReadableByteChannel getChannel() throws IOException {
        if (canDecode()) {
            return ByteUtils.getChannel(getStream());
        } else {
            return getWrappedRepresentation().getChannel();
        }
    }
