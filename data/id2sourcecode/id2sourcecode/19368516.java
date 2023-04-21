    public static void main(String args[]) {
        FileOutputStream fileOutputStream;
        FileChannel fileChannel;
        ByteBuffer byteBuffer;
        try {
            fileOutputStream = new FileOutputStream("D:/work/test.txt");
            fileChannel = fileOutputStream.getChannel();
            byteBuffer = ByteBuffer.allocateDirect(26);
            for (int i = 0; i < 26; i++) byteBuffer.put((byte) ('A' + i));
            byteBuffer.rewind();
            fileChannel.write(byteBuffer);
            fileChannel.close();
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
