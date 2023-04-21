    protected FilePtrMap(RandomAccessFile fp, String fnamp, long baseOffset, long nBytes) throws IOException {
        this.fp = fp;
        this.fnamp = fnamp;
        this.baseOffset = baseOffset;
        this.length = nBytes;
        fc = fp.getChannel();
        mbb = fc.map(FileChannel.MapMode.READ_ONLY, baseOffset, length());
    }
