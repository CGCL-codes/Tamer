    public boolean hasRoom(int n) {
        if (readPos > writePos) {
            return (readPos - writePos) > n;
        }
        return (size - writePos + readPos) > n;
    }
