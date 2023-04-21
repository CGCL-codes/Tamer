    public static void copy(final File src, final File dst) throws IOException {
        if (src.isFile()) {
            FileUtils.copyFile(src, dst);
        } else {
            if (!dst.exists() && !dst.mkdirs()) {
                throw new IOException("Failed to create directory " + dst);
            }
            for (final File s : src.listFiles()) {
                copy(s, new File(dst, s.getName()));
            }
        }
    }
