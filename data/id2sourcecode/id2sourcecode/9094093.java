    private boolean createLogEntity() throws IOException {
        if (file.createNewFile() == false) {
            return false;
        }
        raFile = new RandomAccessFile(file, "rwd");
        fc = raFile.getChannel();
        mappedByteBuffer = fc.map(MapMode.READ_WRITE, 0, this.fileLimitLength);
        mappedByteBuffer.put(MAGIC.getBytes());
        mappedByteBuffer.putInt(version);
        mappedByteBuffer.putInt(nextFile);
        mappedByteBuffer.putInt(endPosition);
        mappedByteBuffer.force();
        this.magicString = MAGIC;
        this.writerPosition = LogEntity.messageStartPosition;
        this.readerPosition = LogEntity.messageStartPosition;
        db.putWriterPosition(this.writerPosition);
        return true;
    }
