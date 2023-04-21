    public FileChannel(java.io.RandomAccessFile file) {
        x.java.io.RandomAccessFile raf = (x.java.io.RandomAccessFile) file;
        this.file = raf.getFile();
        this.channel = raf.getChannel();
        throw new RuntimeException("FIXME - Not tested");
    }
