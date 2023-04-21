    protected void doAppendToSeries(FileHeader header, RoundRobinTimeSeries l) throws SerializationException {
        fileAppendCounter.incrementCount();
        boolean rewriteProperties = header.isPropertiesRewriteRequired();
        if (!shutdown && (l.size() > 0 || rewriteProperties)) {
            File file = getFile(header);
            checkFileWriteable(file);
            RandomAccessFile r = null;
            AuditedFileChannel c = null;
            try {
                r = new RandomAccessFile(file, "rw");
                c = new AuditedFileChannel(r.getChannel(), fileBytesWritten, fileBytesRead);
                if (rewriteProperties) {
                    byte[] properties = header.getPropertiesAsByteArray();
                    int newHeaderLength = getNewHeaderLength(header, properties);
                    if (newHeaderLength > header.getHeaderLength()) {
                        RoundRobinTimeSeries s = serializerOperations.readBody(header, c);
                        s.addAll(l);
                        writeSeries(header, s);
                    } else {
                        serializerOperations.doAppend(header, l, true, properties, c);
                    }
                } else {
                    serializerOperations.doAppend(header, l, false, null, c);
                }
            } catch (Throwable e) {
                fileErrorCounter.incrementCount();
                throw new SerializationException("Failed to append items to file " + header, e);
            } finally {
                flushAndClose(file, c, r);
            }
        }
    }
