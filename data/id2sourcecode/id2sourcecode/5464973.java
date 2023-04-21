    protected RoundRobinTimeSeries doReadSeries(FileHeader fileHeader) throws SerializationException {
        fileReadCounter.incrementCount();
        synchronized (readWriteLock) {
            File f = getFile(fileHeader);
            RandomAccessFile r = null;
            AuditedFileChannel c = null;
            try {
                r = new RandomAccessFile(f, "r");
                c = new AuditedFileChannel(r.getChannel(), fileBytesWritten, fileBytesRead);
                serializerOperations.readHeader(fileHeader, c);
                return serializerOperations.readBody(fileHeader, c);
            } catch (Throwable e) {
                fileErrorCounter.incrementCount();
                throw new SerializationException("Failed to deserialize file " + fileHeader, e);
            } finally {
                flushAndClose(f, c, r);
            }
        }
    }
