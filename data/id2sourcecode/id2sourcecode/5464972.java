    protected void doWriteSeries(FileHeader fileHeader, RoundRobinTimeSeries t) throws SerializationException {
        fileRewriteCounter.incrementCount();
        byte[] properties = fileHeader.getPropertiesAsByteArray();
        int newHeaderLength = getNewHeaderLength(fileHeader, properties);
        int head = t.size() == 0 ? -1 : 0;
        int tail = t.size();
        fileHeader.updateHeaderFields(newHeaderLength, head, tail, t.getMaxSize(), t.getLatestTimestamp());
        File f = getFile(fileHeader);
        AuditedFileChannel b = null;
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(f, "rw");
            b = new AuditedFileChannel(r.getChannel(), fileBytesWritten, fileBytesRead);
            serializerOperations.writeHeader(fileHeader, properties, b);
            serializerOperations.writeBody(t, b);
        } catch (Throwable e) {
            logMethods.error("Failed to write time series file " + f);
            fileErrorCounter.incrementCount();
            throw new SerializationException("Failed to write time series file " + f, e);
        } finally {
            flushAndClose(f, b, r);
        }
    }
