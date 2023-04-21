    @Override
    public ReadableByteChannel getChannel() throws IOException {
        ReadableByteChannel result = this.channel;
        setAvailable(false);
        return result;
    }
