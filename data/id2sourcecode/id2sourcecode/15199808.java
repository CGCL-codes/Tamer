    public NioFixedSegment(File file, int recordLength, boolean readOnly) {
        this.file = file;
        this.recordLength = recordLength;
        final RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, readOnly ? "r" : "rw");
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("File not found: " + file.getAbsolutePath(), e);
        }
        this.channel = raf.getChannel();
    }
