    public String read(String filepath) throws IOException {
        StringBuilder result = new StringBuilder();
        @Cleanup FileChannel file_channel = new FileInputStream(new File(filepath)).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while ((file_channel.read(buffer)) != -1) {
            buffer.flip();
            CharBuffer charBuffer = decoder.decode(buffer);
            result.append(charBuffer.toString());
            buffer.clear();
        }
        return result.toString();
    }
