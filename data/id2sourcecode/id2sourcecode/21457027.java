    private int available(boolean rw) {
        int used = (writex + capacity * 2 - readx) % (capacity * 2);
        if (rw == WRITER) {
            return capacity - used;
        } else {
            return (eof && (used == 0)) ? -1 : used;
        }
    }
