    public void store(int destination, int data) {
        tape.write(ses[destination % ssize].read(), ses[data % ssize].read());
    }
