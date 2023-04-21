    public DefaultBitBuffer(String fileName) {
        File file = new File(fileName);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int fileSize = (int) fc.size();
            ByteBuffer inputByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            fc.close();
            this.byteBuffer = inputByteBuffer;
            bitBufBitSize = ((long) (inputByteBuffer.capacity())) << 3;
            bitPos = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
