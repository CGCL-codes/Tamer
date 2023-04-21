    public Connection(URL url) {
        super(url);
        String file_path = url.getPath();
        this.file = new File(file_path);
        try {
            this.raf = new RandomAccessFile(this.file, "rw");
            this.raf.seek(0L);
            new java.io.FilePermission(file_path, ALLPERMS);
            this.channel = this.raf.getChannel();
        } catch (java.io.IOException exc) {
            throw new alto.sys.Error.State(file_path, exc);
        }
        Shutdown shutdown = new Shutdown(this);
        java.lang.Runtime.getRuntime().addShutdownHook(shutdown);
    }
