    private void init(PersistentByteMap m, File f, int capacity, int strtabSize) throws IOException {
        f.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        {
            BigInteger size = new BigInteger(Integer.toString(((capacity * 3) + 1) / 2));
            BigInteger two = BigInteger.ONE.add(BigInteger.ONE);
            if (size.getLowestSetBit() != 0) size = size.add(BigInteger.ONE);
            while (!size.isProbablePrime(10)) size = size.add(two);
            this.capacity = capacity = size.intValue();
        }
        table_base = 64;
        string_base = table_base + capacity * TABLE_ENTRY_SIZE;
        string_size = 0;
        file_size = string_base;
        elements = 0;
        int totalFileSize = string_base + strtabSize;
        byte[] _4k = new byte[4096];
        for (long i = 0; i < totalFileSize; i += 4096) raf.write(_4k);
        fc = raf.getChannel();
        buf = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
        for (int i = 0; i < capacity; i++) putKeyPos(UNUSED_ENTRY, i);
        putWord(0x67636a64, MAGIC);
        putWord(0x01, VERSION);
        putWord(capacity, CAPACITY);
        putWord(table_base, TABLE_BASE);
        putWord(string_base, STRING_BASE);
        putWord(file_size, FILE_SIZE);
        putWord(elements, ELEMENTS);
        buf.force();
        length = fc.size();
        string_size = 0;
    }
