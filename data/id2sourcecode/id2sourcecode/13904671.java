    public MappedFile(File file) {
        this.file = file;
        try {
            boolean existing = file.exists();
            raf = new RandomAccessFile(file, "rw");
            channel = raf.getChannel();
            if ((!existing) || raf.length() == 0) {
                raf.setLength(minExtendBytes + 8);
                raf.seek(minExtendBytes);
                applicationFileLength = 0;
                raf.writeLong(applicationFileLength);
            } else {
                raf.seek(raf.length() - 8);
                applicationFileLength = raf.readLong();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
