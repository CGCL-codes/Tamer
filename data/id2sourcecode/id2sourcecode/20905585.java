    private String load() throws IOException {
        FileChannel channel = new FileInputStream(uppaalFile).getChannel();
        try {
            ByteBuffer b = ByteBuffer.allocate((int) channel.size());
            channel.read(b);
            return new String(b.array());
        } finally {
            channel.close();
        }
    }
