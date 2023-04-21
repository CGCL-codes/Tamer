    public Archive(String filename, boolean readonly) throws IOException {
        this.readOnly = readonly;
        File f = new File(filename);
        if (!f.exists()) {
            if (readonly) {
                throw new IOException("No such file " + filename);
            }
            file = new RandomAccessFile(filename, "rw");
            initializeFile();
        } else if (readonly && USE_MAP) {
            FileInputStream fs = new FileInputStream(f);
            FileChannel fc = fs.getChannel();
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
        } else {
            file = new RandomAccessFile(filename, readonly ? "r" : "rw");
        }
        if (readOnly && USE_MAP) {
            readHeaderViaMap();
        } else {
            readHeader();
        }
        if (!readOnly) {
            readFreeList();
        }
        if (readOnly && USE_MAP) {
            readDirectoryViaMap();
        } else {
            readDirectory();
        }
    }
