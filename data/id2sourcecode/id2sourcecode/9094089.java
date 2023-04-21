    public LogEntity(String path, LogIndex db, int fileNumber, int fileLimitLength) throws IOException, FileFormatException {
        this.currentFileNumber = fileNumber;
        this.fileLimitLength = fileLimitLength;
        this.db = db;
        file = new File(path);
        if (file.exists() == false) {
            createLogEntity();
            FileRunner.addCreateFile(Integer.toString(fileNumber + 1));
        } else {
            raFile = new RandomAccessFile(file, "rwd");
            if (raFile.length() < LogEntity.messageStartPosition) {
                throw new FileFormatException("file format error");
            }
            fc = raFile.getChannel();
            mappedByteBuffer = fc.map(MapMode.READ_WRITE, 0, this.fileLimitLength);
            byte[] b = new byte[8];
            mappedByteBuffer.get(b);
            magicString = new String(b);
            if (magicString.equals(MAGIC) == false) {
                throw new FileFormatException("file format error");
            }
            version = mappedByteBuffer.getInt();
            nextFile = mappedByteBuffer.getInt();
            endPosition = mappedByteBuffer.getInt();
            if (endPosition == -1) {
                this.writerPosition = db.getWriterPosition();
            } else if (endPosition == -2) {
                this.writerPosition = LogEntity.messageStartPosition;
                db.putWriterPosition(this.writerPosition);
                mappedByteBuffer.position(16);
                mappedByteBuffer.putInt(-1);
                this.endPosition = -1;
            } else {
                this.writerPosition = endPosition;
            }
            if (db.getReaderIndex() == this.currentFileNumber) {
                this.readerPosition = db.getReaderPosition();
            } else {
                this.readerPosition = LogEntity.messageStartPosition;
            }
        }
        executor.execute(new Sync());
    }
