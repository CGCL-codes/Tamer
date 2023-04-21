    @Override
    public void write(OutputStream outputStream) throws IOException {
        write(ByteUtils.getChannel(outputStream));
    }
