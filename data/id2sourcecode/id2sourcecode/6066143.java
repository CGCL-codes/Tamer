    public String read2(String filepath) throws IOException {
        StringBuilder result = new StringBuilder();
        @Cleanup FileChannel file_channel = new FileInputStream(new File(filepath)).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        while ((file_channel.read(buffer)) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                result.append((char) buffer.get());
            }
            buffer.clear();
        }
        return result.toString();
    }
