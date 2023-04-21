    synchronized boolean canWrite() throws IOException {
        if (owners_.size() > 1) {
            return false;
        }
        if ((canRead()) && (writer_ == Thread.currentThread() || writer_ == null)) {
            if (owners_.isEmpty()) {
                return true;
            }
            if (owners_.containsKey(Thread.currentThread())) {
                return true;
            }
        }
        return false;
    }
