    public Semaphore(URL url) {
        super();
        if (null != url) {
            this.url = url;
            String file_path = url.getPath() + ".semaphore";
            try {
                this.file = new RandomAccessFile(file_path, "rw");
                this.file.writeLong(0L);
                new java.io.FilePermission(file_path, ALLPERMS);
                this.channel = this.file.getChannel();
                this.file.setLength(8);
                this.map = this.channel.map(FileChannel.MapMode.READ_WRITE, 0L, 8);
            } catch (java.io.IOException exc) {
                throw new alto.sys.Error.State(file_path, exc);
            }
        } else throw new java.lang.IllegalArgumentException();
    }
