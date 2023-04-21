    public SeekableHttpChannel(SeekableHttpFile file, RangeArrayElement bounds, String store) throws IOException {
        super(bounds);
        this.file = file;
        this.store = store;
        RAEendRepaired = true;
        storeChannel = new RandomAccessFile(store, "rw").getChannel();
    }
