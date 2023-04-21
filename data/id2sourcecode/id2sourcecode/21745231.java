    private synchronized void verifyBufferSize(int count) {
        if (count > (buf.length - writepos)) {
            System.arraycopy(buf, readpos, buf, 0, writepos - readpos);
            writepos -= readpos;
            readpos = 0;
        }
        if (count > (buf.length - writepos)) {
            byte[] tmp = new byte[buf.length + DEFAULT_BUFFER_SIZE];
            System.arraycopy(buf, 0, tmp, 0, writepos - readpos);
            buf = tmp;
        }
    }
