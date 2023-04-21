    public PersistentByteMap(File f, AccessMode mode) throws IOException {
        name = f;
        if (mode == AccessMode.READ_ONLY) {
            FileInputStream fis = new FileInputStream(f);
            fc = fis.getChannel();
        } else {
            RandomAccessFile fos = new RandomAccessFile(f, "rw");
            fc = fos.getChannel();
        }
        length = fc.size();
        buf = fc.map(mode.mapMode, 0, length);
        int magic = getWord(MAGIC);
        if (magic != 0x67636a64) throw new IllegalArgumentException(f.getName());
        table_base = getWord(TABLE_BASE);
        capacity = getWord(CAPACITY);
        string_base = getWord(STRING_BASE);
        string_size = getWord(STRING_SIZE);
        file_size = getWord(FILE_SIZE);
        elements = getWord(ELEMENTS);
    }
