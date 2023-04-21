    synchronized boolean canRead() throws IOException {
        if (writer_ != null && writer_ != Thread.currentThread()) {
            return false;
        }
        if (writer_ == null) {
            return true;
        }
        if (owners_.size() > 1) {
            return false;
        }
        return true;
    }
