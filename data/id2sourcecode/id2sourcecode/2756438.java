    synchronized void write(int b) throws IOException {
        synchronized (writeLock) {
            if (readerClosed || writerClosed) throw new IOException("Stream closed");
            while (availableSpace() == 0) {
                if (growBuf) growBuf(1); else try {
                    wait();
                } catch (InterruptedException e) {
                    throw new InterruptedIOException();
                }
            }
            if (readerClosed || writerClosed) throw new IOException("Stream closed");
            buf[writeIndex++] = (byte) (b & 0xff);
            if (writeIndex == buf.length) writeIndex = 0;
            notifyAll();
        }
    }
