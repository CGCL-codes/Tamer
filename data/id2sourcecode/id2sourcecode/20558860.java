    public int read(byte[] b, int off, int len) throws IOException {
        boolean bReady = false;
        int iRead = 0;
        synchronized (buffer) {
            while (!bReady) {
                if (source == null) {
                    throw new IOException("Unconnected pipe");
                }
                if (writePosition == readPosition && writeLaps == readLaps) {
                    if (closed) {
                        return -1;
                    }
                    try {
                        buffer.notifyAll();
                        buffer.wait();
                    } catch (InterruptedException e) {
                        throw new IOException(e.getMessage());
                    }
                    continue;
                }
                int amount = Math.min(len, (writePosition > readPosition ? writePosition : buffer.length) - readPosition);
                System.arraycopy(buffer, readPosition, b, off, amount);
                readPosition += amount;
                iRead += amount;
                if (readPosition == buffer.length) {
                    readPosition = 0;
                    ++readLaps;
                }
                if (amount < len) {
                    off = off + amount;
                    len = len - amount;
                } else {
                    bReady = true;
                    buffer.notifyAll();
                }
            }
        }
        return iRead;
    }
