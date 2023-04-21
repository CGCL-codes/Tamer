    public void write(byte[] b, int off, int len) throws IOException {
        boolean bReady = false;
        synchronized (sink.buffer) {
            while (!bReady) {
                if (sink == null) {
                    throw new IOException("Unconnected pipe");
                }
                if (sink.closed) {
                    throw new IOException("Broken pipe");
                }
                if (sink.writePosition == sink.readPosition && sink.writeLaps > sink.readLaps) {
                    try {
                        sink.buffer.notifyAll();
                        sink.buffer.wait();
                    } catch (InterruptedException e) {
                        throw new IOException(e.getMessage());
                    }
                    continue;
                }
                int amount = Math.min(len, (sink.writePosition < sink.readPosition ? sink.readPosition : sink.buffer.length) - sink.writePosition);
                System.arraycopy(b, off, sink.buffer, sink.writePosition, amount);
                sink.writePosition += amount;
                if (sink.writePosition == sink.buffer.length) {
                    sink.writePosition = 0;
                    ++sink.writeLaps;
                }
                if (amount < len) {
                    off = off + amount;
                    len = len - amount;
                } else {
                    bReady = true;
                    sink.buffer.notifyAll();
                }
            }
        }
    }
