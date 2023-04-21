    @Override
    public void write(OutputStream outputStream) throws IOException {
        write(NioUtils.getChannel(outputStream));
    }
