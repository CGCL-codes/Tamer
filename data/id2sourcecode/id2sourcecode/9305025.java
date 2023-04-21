    public synchronized boolean isEmpty() {
        if (readIndex == writeIndex) {
            return true;
        }
        return false;
    }
