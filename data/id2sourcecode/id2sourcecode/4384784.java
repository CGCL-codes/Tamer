    public boolean doWrite() throws IOException {
        synchronized (writeLock) {
            ByteBuffer buffer = null;
            int wrote = 0;
            int message = 0;
            while ((buffer = _outQueue.getNonBlocking()) != null) {
                wrote += this.getChannel().write(buffer);
                if (buffer.remaining() > 0) {
                    _outQueue.prepend(buffer);
                    return false;
                } else {
                    message++;
                }
            }
            return true;
        }
    }
