    public synchronized void seekAndWrite(long pos, byte[] b, int off, int len) throws IOException {
        if (e != null) {
            throw e;
        }
        if (len != 0) {
            Range r = new Range(pos, pos + len - 1);
            RangeSet rs = new RangeSet();
            rs.add(r);
            if (!committed.intersect(rs).isEmpty()) {
                throw new IOException("Illegal write attempt.  Parts of range " + "already committed. :" + r);
            }
        }
        _raf.seekAndWrite(pos, b, off, len);
        if (len == 0) {
            return;
        }
        fillBlockedBuffers(pos, b, off, len);
    }
