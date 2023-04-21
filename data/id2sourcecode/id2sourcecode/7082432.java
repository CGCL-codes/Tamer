    public synchronized void transferToStream(long token, OutputStream out) {
        byte[] buf = Util.takeThreadLocalBuf();
        try {
            atEnd = false;
            file.seek(token);
            int length = file.readInt();
            int bufLen = buf.length;
            while (length > bufLen) {
                int read = file.read(buf, 0, bufLen);
                length -= read;
                out.write(buf, 0, read);
            }
            while (length > 0) {
                int read = file.read(buf, 0, length);
                length -= read;
                out.write(buf, 0, read);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read from byte cache", e);
        } finally {
            Util.releaseThreadLocalBuf(buf);
        }
    }
