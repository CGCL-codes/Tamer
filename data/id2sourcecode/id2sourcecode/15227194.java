    protected final void pop() {
        currentSize--;
        for (int i = 0; i < currentSize; i++) tokens[i] = tokens[i + 1];
        tokens[currentSize] = null;
    }
