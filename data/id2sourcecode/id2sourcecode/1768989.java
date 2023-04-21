    public int read(byte[] b, int off, int len) throws IOException {
        if (len <= 0) {
            return 0;
        }
        long start = fp;
        file.open(openFlags, SmbFile.ATTR_NORMAL, 0);
        if (DebugFile.trace) DebugFile.writeln("read: fid=" + file.fid + ",off=" + off + ",len=" + len);
        SmbComReadAndXResponse response = new SmbComReadAndXResponse(b, off);
        if (file.type == SmbFile.TYPE_NAMED_PIPE) {
            response.responseTimeout = 0;
        }
        int r, n;
        do {
            r = len > readSize ? readSize : len;
            if (DebugFile.trace) DebugFile.writeln("read: len=" + len + ",r=" + r + ",fp=" + fp);
            try {
                file.send(new SmbComReadAndX(file.fid, fp, r, null), response);
            } catch (SmbException se) {
                if (file.type == SmbFile.TYPE_NAMED_PIPE && se.getNtStatus() == NtStatus.NT_STATUS_PIPE_BROKEN) {
                    return -1;
                }
                throw se;
            }
            if ((n = response.dataLength) <= 0) {
                return (int) ((fp - start) > 0L ? fp - start : -1);
            }
            fp += n;
            len -= n;
            response.off += n;
        } while (len > 0 && n == r);
        return (int) (fp - start);
    }
