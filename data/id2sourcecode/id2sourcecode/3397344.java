    void moveTokens() {
        int n = write - read;
        if (read > n) {
            if (n > 0) {
                System.arraycopy(contents, read, contents, 0, n);
            }
            read = 0;
            write = n;
        }
    }
