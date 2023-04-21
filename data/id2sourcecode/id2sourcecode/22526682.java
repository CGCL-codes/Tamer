    private void checkThread(boolean write) {
        if (!isAllowedThread(write)) {
            throw new IllegalStateException("Current thread is not allowed to obtain a " + (write ? "write" : "read") + " lock on " + this);
        }
    }
