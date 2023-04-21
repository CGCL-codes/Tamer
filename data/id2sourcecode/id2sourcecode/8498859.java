    public LogIndex(String path) throws IOException, FileFormatException {
        File dbFile = new File(path);
        if (dbFile.exists() == false) {
            dbFile.createNewFile();
            dbRandFile = new RandomAccessFile(dbFile, "rwd");
            dbRandFile.write(LogEntity.MAGIC.getBytes());
            dbRandFile.writeInt(1);
            dbRandFile.writeInt(LogEntity.messageStartPosition);
            dbRandFile.writeInt(LogEntity.messageStartPosition);
            dbRandFile.writeInt(1);
            dbRandFile.writeInt(1);
            dbRandFile.writeInt(0);
            magicString = LogEntity.MAGIC;
            version = 1;
            readerPosition = LogEntity.messageStartPosition;
            writerPosition = LogEntity.messageStartPosition;
            readerIndex = 1;
            writerIndex = 1;
        } else {
            dbRandFile = new RandomAccessFile(dbFile, "rwd");
            if (dbRandFile.length() < 32) {
                throw new FileFormatException("file format error");
            }
            byte[] b = new byte[this.dbFileLimitLength];
            dbRandFile.read(b);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            b = new byte[LogEntity.MAGIC.getBytes().length];
            buffer.get(b);
            magicString = new String(b);
            version = buffer.getInt();
            readerPosition = buffer.getInt();
            writerPosition = buffer.getInt();
            readerIndex = buffer.getInt();
            writerIndex = buffer.getInt();
            size.set(buffer.getInt());
        }
        fc = dbRandFile.getChannel();
        mappedByteBuffer = fc.map(MapMode.READ_WRITE, 0, this.dbFileLimitLength);
    }
