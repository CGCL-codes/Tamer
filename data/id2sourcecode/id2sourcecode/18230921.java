    public BinaryFS(String binaryFile) throws IOException {
        this.binaryFile = binaryFile;
        RandomAccessFile file = new RandomAccessFile(binaryFile, "r");
        long len = file.length();
        MappedByteBuffer buff = file.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, len);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        file.close();
        byte[] magic = new byte[MAGIC.length];
        buff.get(magic);
        if (!Arrays.equals(magic, MAGIC)) {
            throw new IOException("Bad magic header: " + new String(magic));
        }
        long storedLen = buff.getInt();
        if (len != storedLen) {
            throw new IOException("Corrupted image, correct length=" + storedLen);
        }
        int stop = buff.getInt() + 8 + MAGIC.length;
        modifications = new ArrayList();
        while (buff.position() < stop) {
            modifications.add(getString(buff));
        }
        content = buff.slice().order(ByteOrder.LITTLE_ENDIAN);
        root = new BFSFolder("", null, 0);
    }
