    private void write(String filePath, String content, boolean append) throws Exception {
        @Cleanup FileChannel file_channel = new FileOutputStream(new File(filePath), append).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(content.length());
        buffer.clear();
        buffer.put(content.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            file_channel.write(buffer);
        }
    }
