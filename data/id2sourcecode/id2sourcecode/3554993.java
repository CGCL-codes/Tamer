    public PhysTableEntity(File fileHnd) throws IOException {
        if (!fileHnd.exists()) fileHnd.createNewFile();
        this.fileHnd = fileHnd;
        file = new RandomAccessFile(fileHnd, "rw");
        channel = file.getChannel();
        lock = channel.lock();
        position = 0;
    }
