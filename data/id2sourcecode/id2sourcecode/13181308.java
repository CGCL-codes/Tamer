    public void load(int source, int destination) {
        ses[destination % ssize].write(tape.read(ses[source % ssize].read()));
    }
