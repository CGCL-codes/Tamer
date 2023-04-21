    public boolean isFull() {
        if (writeIndex + 1 <= buffer.capacity() && writeIndex + 1 == readIndex) return true;
        if (writeIndex == buffer.capacity() - 1 && readIndex == 0) return true;
        return false;
    }
