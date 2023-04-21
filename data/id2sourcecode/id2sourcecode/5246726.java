    public void close() throws SeisException {
        LOG.fine("TraceMap reads = " + _readCounter + " read cached hits " + _readCacheHit + " TraceMap writes = " + _writeCounter);
        if (_mapIO instanceof BufferedVirtualIO) {
            long[] stats = ((BufferedVirtualIO) _mapIO).getWriteStats();
            LOG.info("writes = " + stats[0] + " actual disk writes = " + stats[1] + " gaps filled = " + stats[2] + " gaps larger than a single block = " + stats[3]);
        }
        _mapIO.close();
    }
