    public SeekableHttpChannel(SeekableHttpFile file, RangeArrayElement requestRange) throws IOException {
        super(requestRange.starting(), requestRange.starting(), requestRange.ending());
        this.file = file;
        store = file.store + File.separator + Math.random() + "_0x" + Long.toHexString(requestRange.starting()) + ".partial";
        RAEendRepaired = false;
        storeChannel = new RandomAccessFile(store, "rw").getChannel();
    }
