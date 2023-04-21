    public void iterationInit() throws IOException {
        this.iterlock.lock();
        try {
            if (Main.hashLength == 16) {
                hc = new Tiger16HashEngine();
            } else {
                hc = new TigerHashEngine();
            }
            this.iterFC = new RandomAccessFile(f, "r").getChannel();
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            this.iterlock.unlock();
        }
    }
