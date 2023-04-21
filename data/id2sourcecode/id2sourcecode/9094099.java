    public byte[] readNextAndRemove() throws FileEOFException {
        if (this.endPosition != -1 && this.readerPosition >= this.endPosition) {
            throw new FileEOFException("file eof");
        }
        if (this.readerPosition >= this.writerPosition) {
            return null;
        }
        mappedByteBuffer.position(this.readerPosition);
        int length = mappedByteBuffer.getInt();
        byte[] b = new byte[length];
        this.readerPosition += length + 4;
        mappedByteBuffer.get(b);
        putReaderPosition(this.readerPosition);
        return b;
    }
