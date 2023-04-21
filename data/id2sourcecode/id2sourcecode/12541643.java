    private int getValueSecurityCode(boolean read, boolean write, boolean delete, boolean full) {
        int val = 0;
        if (read) val += 1;
        if (write) val += 2;
        if (delete) val += 4;
        if (full) val += 8;
        return val;
    }
