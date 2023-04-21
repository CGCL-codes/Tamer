    DataFile(File file, boolean force) throws IOException {
        this.file = file;
        fc = new RandomAccessFile(file, force ? "rws" : "rw").getChannel();
        fc.position(fc.size());
    }
