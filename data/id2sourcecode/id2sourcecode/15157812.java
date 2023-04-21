    public ArrayFile openRW() throws FileNotFoundException, IOException {
        if (channel != null) throw new IOException("ArrayFile already open");
        channel = new RandomAccessFile(name, "rw").getChannel();
        mode = null;
        return this;
    }
