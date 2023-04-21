    public static void testFile() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\test2.rtf");
        FileChannel channel = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(100);
        int i = channel.read(buffer);
        channel.close();
        System.out.println(new String(buffer.array()));
    }
