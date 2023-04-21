    protected void open() throws IOException {
        synchronized (streamLock) {
            stream = new FileOutputStream(file);
            channel = stream.getChannel();
        }
    }
