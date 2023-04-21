    public synchronized void reset() throws IOException {
        RAF.getChannel().truncate(0);
        FirstKey = Long.MIN_VALUE;
        LastKey = Long.MIN_VALUE;
    }
