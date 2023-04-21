    public BitFieldFile(File f) {
        try {
            raf = new RandomAccessFile(f, "rw");
            fileLength = (int) raf.length();
            channel = raf.getChannel();
            reopen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
