    public void dec(int what) {
        ses[what % ssize].write((short) (ses[what % ssize].read() - 1));
    }
