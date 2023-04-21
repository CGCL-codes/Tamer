    public static synchronized void setTemporaryDirectory(File directory) throws IOException {
        if (!directory.isDirectory()) {
            throw new IOException(directory.getAbsolutePath() + " is not a directory. Cannot set as temp directory.");
        }
        if (!directory.canWrite() || !directory.canRead()) {
            throw new IOException(directory.getAbsolutePath() + " has constricting permissions (readable?=" + directory.canRead() + ", writeable?=" + directory.canWrite() + "). Cannot set as temp directory.");
        }
        if (tempDirectory.getAbsolutePath().equals(directory.getAbsolutePath())) {
            return;
        }
        if (tempDirectory != null) {
            clear();
        }
        tempDirectory = directory;
    }
