    private void initChannel() throws IOException {
        if (channel != null) return;
        synchronized (this) {
            if (channel != null) return;
            raf = new RandomAccessFile(file, "r");
            channel = raf.getChannel();
        }
    }
