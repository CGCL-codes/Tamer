    public FileChannel getFileChannel() {
        if (ischunked) return null;
        if (in instanceof FileInputStream) return ((FileInputStream) in).getChannel();
        return null;
    }
