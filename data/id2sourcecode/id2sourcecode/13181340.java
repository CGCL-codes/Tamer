    public void rel_load(int base, int offset, int destination) {
        ses[destination % ssize].write(tape.read(ses[base % ssize].read() + ses[offset % ssize].read()));
    }
