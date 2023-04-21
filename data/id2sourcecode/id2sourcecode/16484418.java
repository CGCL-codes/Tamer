    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException("Stream is closed.");
        }
        synchronized (this) {
            buf[writeIndex++] = (byte) (b & 0xff);
            if (writeIndex == buf.length) {
                writeIndex = 0;
            }
            if (writeIndex == readIndex) {
                if (skipLines) {
                    setReadIndexToNextLine();
                } else {
                    throw new IOException("Buffer is full.");
                }
            }
            if (waiting) {
                notifyAll();
            }
        }
    }
