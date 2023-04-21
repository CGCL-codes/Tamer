    private Connection(URL url, boolean isClient, int size) {
        super(url);
        this.isClient = isClient;
        String file_path = url.getPath();
        this.file = new File(file_path);
        boolean found = (this.file.exists());
        try {
            this.raf = new RandomAccessFile(this.file, "rw");
            this.raf.setLength(size);
            this.raf.seek(0L);
            if (this.isClient || (!found)) {
                this.raf.write(Connection.Client.RQP_WRITE);
                this.raf.seek(0L);
            }
            new java.io.FilePermission(file_path, ALLPERMS);
            this.channel = this.raf.getChannel();
            this.map = this.channel.map(FileChannel.MapMode.READ_WRITE, 0L, size);
            this.in = new InputStream(this.map, SIZEOF_SEMAPHORE);
            this.out = new OutputStream(this.map, SIZEOF_SEMAPHORE);
        } catch (java.io.IOException exc) {
            throw new alto.sys.Error.State(file_path, exc);
        }
        this.shutdown = new Shutdown(this);
        java.lang.Runtime.getRuntime().addShutdownHook(this.shutdown);
    }
