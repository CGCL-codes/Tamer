    @MutableCopy
    public static List<Loadable> load(File propFile, Class<?> containerClass, boolean createIfMissing) throws IOException, FileNotFoundException {
        if (!propFile.exists()) {
            if (createIfMissing) propFile.createNewFile(); else throw new FileNotFoundException();
        }
        InputStream in = null;
        try {
            FileInputStream fin = new FileInputStream(propFile);
            FileLock lock = fin.getChannel().lock(0, Long.MAX_VALUE, true);
            try {
                in = new BufferedInputStream(fin);
                return load(in, containerClass);
            } finally {
                lock.release();
            }
        } finally {
            Closeables.closeQuietly(in);
        }
    }
