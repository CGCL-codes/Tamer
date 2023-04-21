    public FileChannel getChannel() {
        if (raf == null) {
            throw new IllegalStateException("This file does not have a " + "RandomAccessFile. It was probably created for unit testing.");
        }
        return raf.getChannel();
    }
