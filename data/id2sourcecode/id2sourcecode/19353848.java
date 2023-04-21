    public void setBaseDirectory(String dir) {
        File temp = new File(dir);
        if (!temp.exists()) {
            if (!temp.mkdirs()) {
                throw new IllegalArgumentException("Could not create base directory " + dir + ".");
            }
        }
        if (!temp.canRead() || !temp.canWrite()) {
            throw new IllegalStateException("Cannot read/write directory " + dir + ".");
        }
        baseDirectory = temp;
    }
