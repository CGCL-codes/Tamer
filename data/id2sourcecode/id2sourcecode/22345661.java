    protected synchronized void receive(double d) throws IOException {
        checkStateForReceive();
        writeSide = Thread.currentThread();
        if (in == out) awaitSpace();
        if (in < 0) {
            in = 0;
            out = 0;
        }
        buffer[in++] = d;
        if (in >= buffer.length) {
            in = 0;
        }
    }
