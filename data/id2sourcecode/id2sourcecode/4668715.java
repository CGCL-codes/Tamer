    @Override
    public void write(WritableByteChannel writableChannel) throws IOException {
        ByteUtils.write(getChannel(), writableChannel);
    }
