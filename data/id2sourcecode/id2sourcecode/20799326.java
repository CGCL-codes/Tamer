    protected void setUp() throws IOException {
        tmpFile = File.createTempFile("harmony", "test");
        tmpFile.deleteOnExit();
        FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(26 + 20);
        for (int i = 0; i < 26; i++) {
            byteBuffer.put((byte) ('A' + i));
        }
        for (int i = 0; i < 5; i++) {
            byteBuffer.putInt(i + 1);
        }
        byteBuffer.rewind();
        fileChannel.write(byteBuffer);
        fileChannel.close();
        fileOutputStream.close();
        emptyFile = File.createTempFile("harmony", "test");
        emptyFile.deleteOnExit();
    }
