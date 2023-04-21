    @Override
    public void write(WritableByteChannel writableChannel) throws IOException {
        NioUtils.copy(getChannel(), writableChannel);
    }
