    public static String getBodyByChannel(String filepath) throws FileNotFoundException, IOException {
        ByteBuffer byteBuf = ByteBuffer.allocate(500);
        StringBuilder builder = new StringBuilder();
        @Cleanup FileChannel file_channel = new FileInputStream(new File(filepath)).getChannel();
        while (file_channel.read(byteBuf) != -1) {
            byteBuf.flip();
            builder.append(byteBuf.array());
            byteBuf.clear();
        }
        return builder.toString();
    }
