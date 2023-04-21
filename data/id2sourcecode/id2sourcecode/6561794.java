    public void open(File file) throws IOException {
        if (file.canWrite()) {
            try {
                raf = new RandomAccessFile(file, "rw");
                mode = FileChannel.MapMode.READ_WRITE;
            } catch (FileNotFoundException e) {
                raf = new RandomAccessFile(file, "r");
                mode = FileChannel.MapMode.READ_ONLY;
            }
        } else {
            raf = new RandomAccessFile(file, "r");
            mode = FileChannel.MapMode.READ_ONLY;
        }
        channel = raf.getChannel();
        buffer = new BigByteBuffer(channel, mode);
        myHeader = new DbaseFileHeader();
        myHeader.readHeader(buffer);
    }
