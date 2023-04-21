    public IDFile(File file, boolean forceSync) throws IOException {
        this.file = file;
        this.forceSync = forceSync;
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Failed to create file: " + file);
            }
        }
        raf = new RandomAccessFile(file, "rw");
        fileChannel = raf.getChannel();
        if (fileChannel.size() == 0L) {
            ByteBuffer buf = ByteBuffer.allocate((int) HEADER_LENGTH);
            buf.put(MAGIC_NUMBER);
            buf.put(FILE_FORMAT_VERSION);
            buf.put(new byte[] { 0, 0, 0, 0 });
            buf.rewind();
            fileChannel.write(buf, 0L);
            sync();
        } else {
            ByteBuffer buf = ByteBuffer.allocate((int) HEADER_LENGTH);
            fileChannel.read(buf, 0L);
            buf.rewind();
            if (buf.remaining() < HEADER_LENGTH) {
                throw new IOException("File too short to be a compatible ID file");
            }
            byte[] magicNumber = new byte[MAGIC_NUMBER.length];
            buf.get(magicNumber);
            byte version = buf.get();
            if (!Arrays.equals(MAGIC_NUMBER, magicNumber)) {
                throw new IOException("File doesn't contain compatible ID records");
            }
            if (version > FILE_FORMAT_VERSION) {
                throw new IOException("Unable to read ID file; it uses a newer file format");
            } else if (version != FILE_FORMAT_VERSION) {
                throw new IOException("Unable to read ID file; invalid file format version: " + version);
            }
        }
    }
