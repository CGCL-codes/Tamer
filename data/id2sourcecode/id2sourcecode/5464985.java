    private void readHeaderImpl(FileHeader fileHeader, File f) throws SerializationException {
        fileHeaderReadCounter.incrementCount();
        RandomAccessFile r = null;
        AuditedFileChannel c = null;
        try {
            r = new RandomAccessFile(f, "r");
            c = new AuditedFileChannel(r.getChannel(), fileBytesWritten, fileBytesRead);
            serializerOperations.readHeader(fileHeader, c);
        } catch (Throwable e) {
            fileErrorCounter.incrementCount();
            throw new SerializationException("Failed to deserialize header " + fileHeader, e);
        } finally {
            flushAndClose(f, c, r);
        }
    }
