    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (source == null) {
            throw new IOException("Unconnected pipe");
        }
        synchronized (buffer) {
            if (writePosition == readPosition && writeLaps == readLaps) {
                if (closed) {
                    return -1;
                }
                try {
                    buffer.wait();
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
                return read(b, off, len);
            }
            int amount = Math.min(len, (writePosition > readPosition ? writePosition : buffer.length) - readPosition);
            System.arraycopy(buffer, readPosition, b, off, amount);
            readPosition += amount;
            if (readPosition == buffer.length) {
                readPosition = 0;
                ++readLaps;
            }
            if (amount < len) {
                int second = read(b, off + amount, len - amount);
                return second == -1 ? amount : amount + second;
            } else {
                buffer.notifyAll();
            }
            return amount;
        }
    }
