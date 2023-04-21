    int numPending() {
        return writePtr - readPtr;
    }
