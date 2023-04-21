    public void close() {
        try {
            this.closed = true;
            this.closeStore();
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            raf.getChannel().force(true);
        } catch (Exception e) {
            SDFSLogger.getLog().warn("while closing filechunkstore ", e);
        }
    }
