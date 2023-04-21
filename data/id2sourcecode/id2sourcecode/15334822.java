    public int write(DataReader reader) throws IOException {
        if (!tail.buffer.hasRemaining()) {
            newTail();
        }
        ByteBuffer bb = tail.buffer;
        int pos = bb.position();
        int len = bb.remaining();
        int nread = reader.read(bb.array(), pos, len);
        if (nread <= 0) {
            reader.close();
            return -1;
        }
        bb.position(pos + nread);
        synchronized (this) {
            position += nread;
            notifyAll();
        }
        return nread;
    }
