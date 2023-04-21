    public void shift_r(int op1, int result) {
        ses[result % ssize].write((short) ((ses[op1 % ssize].read()) >> 1));
    }
