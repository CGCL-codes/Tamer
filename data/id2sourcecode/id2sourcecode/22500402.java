    protected synchronized void receive(short s) throws IOException {
        checkStateForReceive();
        writeSide = Thread.currentThread();
        if (in == out) awaitSpace();
        if (in < 0) {
            in = 0;
            out = 0;
        }
        buffer[in++] = s;
        if (in >= buffer.length) {
            in = 0;
        }
    }
