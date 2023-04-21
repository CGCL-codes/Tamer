    public boolean hasTokens(int n) {
        if (writePos >= readPos) {
            return (writePos - readPos) >= n;
        } else {
            return (size - readPos + writePos) >= n;
        }
    }
