    public void rel_store(int base, int offset, int from) {
        tape.write(ses[base % ssize].read() + ses[offset % ssize].read(), ses[from % ssize].read());
    }
