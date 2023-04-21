    public void cinc(int n) {
        ses[n % ssize].write((short) ((ses[n % ssize].read() + 1) % tape.getSize()));
    }
