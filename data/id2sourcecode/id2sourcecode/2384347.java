    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("E:/google-code/trunk/java-tourist/src/main/resources/testFile.txt", "rw");
        FileChannel inChannel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int bytesRead = inChannel.read(buffer);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }
            buffer.clear();
            bytesRead = inChannel.read(buffer);
        }
        file.close();
    }
