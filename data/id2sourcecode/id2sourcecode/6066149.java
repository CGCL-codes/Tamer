    private void writeShit(String filePath, String content, boolean append) throws Exception {
        @Cleanup FileChannel file_channel = new FileOutputStream(new File(filePath), append).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < content.length(); i++) {
            buffer.putChar(content.charAt(i));
        }
        buffer.flip();
        file_channel.write(buffer);
        buffer.clear();
    }
