    private static File createTempFolder(final String tempFolder) {
        File f = new File(tempFolder);
        if (!f.canRead() || !f.canWrite()) {
            throw new IllegalStateException("Can't read from or write to the temp folder: " + f.getAbsolutePath());
        }
        return f;
    }
