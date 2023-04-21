    protected synchronized void receive(long l) throws IOException {
        checkStateForReceive();
        writeSide = Thread.currentThread();
        if (in == out) awaitSpace();
        if (in < 0) {
            in = 0;
            out = 0;
        }
        buffer[in++] = l;
        if (in >= buffer.length) {
            in = 0;
        }
    }
