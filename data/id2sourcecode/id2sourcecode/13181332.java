    public void shift_l(int op1, int result) {
        ses[result % ssize].write((short) ((ses[op1 % ssize].read()) << 1));
    }
