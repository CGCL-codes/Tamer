    @Override
    public ReadableByteChannel getChannel() throws IOException {
        return getWrappedRepresentation().getChannel();
    }
