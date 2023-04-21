    public void mod(int op1, int op2, int result) {
        short divider = ses[op2 % ssize].read();
        if (divider == 0) return;
        ses[result % ssize].write((short) (ses[op1 % ssize].read() % divider));
    }
