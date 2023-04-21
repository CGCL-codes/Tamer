    public void open(File file, int width, int height, boolean alpha) throws IOException {
        RandomAccessFile out = new RandomAccessFile(file, "rw");
        ch = out.getChannel();
        int pixelSize = (alpha ? 32 : 24);
        int numChannels = (alpha ? 4 : 3);
        int fileLength = TARGA_HEADER_SIZE + width * height * numChannels;
        out.setLength(fileLength);
        MappedByteBuffer image = ch.map(FileChannel.MapMode.READ_WRITE, 0, fileLength);
        image.put(0, (byte) 0).put(1, (byte) 0);
        image.put(2, (byte) 2);
        image.put(12, (byte) (width & 0xFF));
        image.put(13, (byte) (width >> 8));
        image.put(14, (byte) (height & 0xFF));
        image.put(15, (byte) (height >> 8));
        image.put(16, (byte) pixelSize);
        image.position(TARGA_HEADER_SIZE);
        buf = image.slice();
    }
