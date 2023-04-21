    @Test
    public void testFileChannel() throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("pom.xml", "rw");
        FileChannel channel = aFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        int bytesRead = channel.read(buffer);
        while (bytesRead != -1) {
            System.err.println("Read " + bytesRead);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.err.println((char) buffer.get());
            }
            buffer.clear();
            bytesRead = channel.read(buffer);
        }
        aFile.close();
    }
