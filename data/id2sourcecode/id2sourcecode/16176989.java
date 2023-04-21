    public static void main(String args[]) {
        FileInputStream fileInputStream;
        FileChannel fileChannel;
        long fileSize;
        MappedByteBuffer mBuffer;
        try {
            fileInputStream = new FileInputStream("D:/work/test.txt");
            fileChannel = fileInputStream.getChannel();
            fileSize = fileChannel.size();
            mBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            for (int i = 0; i < fileSize; i++) {
                System.out.print((char) mBuffer.get());
            }
            fileChannel.close();
            fileInputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
