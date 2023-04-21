    public void not(int op1, int result) {
        ses[result % ssize].write((short) (~(ses[op1 % ssize].read())));
    }
