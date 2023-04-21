    public static final void checkDir(String dir, boolean read, boolean write) throws SecurityException, IOException {
        File f = new File(dir);
        if (!f.exists()) {
            f.mkdir();
            if (read && !f.canRead()) {
                f.delete();
                throw new IOException("Cannot read from " + dir);
            }
            if (write && !f.canWrite()) {
                f.delete();
                throw new IOException("Cannot write to " + dir);
            }
        } else if (!f.isDirectory()) {
            throw new IOException(dir + " is not a directory");
        } else if (read && !f.canRead()) {
            throw new IOException("Cannot read from " + dir);
        } else if (write && !f.canWrite()) {
            throw new IOException("Cannot write to " + dir);
        }
        return;
    }
