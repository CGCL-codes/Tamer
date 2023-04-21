    private synchronized int bytesUsed() {
        if (readOfs == writeOfs) return 0;
        if (readOfs < writeOfs) return writeOfs - readOfs;
        return buf.length - (writeOfs - readOfs);
    }
