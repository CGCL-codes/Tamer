    public void write(byte[] b, int off, int len) throws IOException {
        if (sink == null) {
            throw new IOException("Unconnected pipe");
        }
        if (sink.closed) {
            throw new IOException("Broken pipe");
        }
        synchronized (sink.buffer) {
            if (sink.writePosition == sink.readPosition && sink.writeLaps > sink.readLaps) {
                try {
                    sink.buffer.wait();
                } catch (InterruptedException e) {
                    throw new IOException(e.getMessage());
                }
                write(b, off, len);
                return;
            }
            int amount = Math.min(len, (sink.writePosition < sink.readPosition ? sink.readPosition : sink.buffer.length) - sink.writePosition);
            System.arraycopy(b, off, sink.buffer, sink.writePosition, amount);
            sink.writePosition += amount;
            if (sink.writePosition == sink.buffer.length) {
                sink.writePosition = 0;
                ++sink.writeLaps;
            }
            if (amount < len) {
                write(b, off + amount, len - amount);
            } else {
                sink.buffer.notifyAll();
            }
        }
    }
