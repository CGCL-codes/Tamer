    public void prepare() throws IOException {
        if (fc == null) fc = new RandomAccessFile(fn, "r").getChannel();
        length = fc.size();
        position = 0;
    }
