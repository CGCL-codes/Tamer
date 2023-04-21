    public ReadableByteChannel getChannel() throws IOException {
        RandomAccessFile rafile = new RandomAccessFile(this.file, "r");
        return rafile.getChannel();
    }
