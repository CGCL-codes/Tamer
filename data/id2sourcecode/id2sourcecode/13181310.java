    public void move(int source, int destination) {
        ses[destination % ssize].write(ses[source % ssize].read());
    }
