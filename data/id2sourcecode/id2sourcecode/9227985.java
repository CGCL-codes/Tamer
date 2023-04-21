    protected synchronized void receive(int b) throws IOException {
        writeSide = Thread.currentThread();
        while (in == out) {
            notifyAll();
            try {
                wait(1000);
            } catch (InterruptedException ex) {
                throw new java.io.InterruptedIOException();
            }
        }
        if (in < 0) {
            in = 0;
            out = 0;
        }
        buffer[in++] = (byte) (b & 0xFF);
        if (in >= buffer.length) {
            in = 0;
        }
    }
