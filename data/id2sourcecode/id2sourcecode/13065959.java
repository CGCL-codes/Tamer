    private void writeBinaryData() throws FileNotFoundException, IOException {
        final FileChannel channel = new FileInputStream(this.file).getChannel();
        final ByteBuffer allocate = ByteBuffer.allocate((int) channel.size());
        channel.read(allocate);
        this.response.getOutputStream().write(allocate.array());
    }
