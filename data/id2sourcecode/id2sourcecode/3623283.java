    private void obtainLock() throws IOException {
        if (lock != null) {
            return;
        }
        if (!Options.getLockFile().exists()) {
            Options.getLockFile().getParentFile().mkdirs();
        }
        RandomAccessFile raf = new RandomAccessFile(Options.getLockFile(), "rw");
        lock = raf.getChannel().lock();
        if (Options.isDebug()) {
            System.out.println("Lock obtained.");
        }
    }
