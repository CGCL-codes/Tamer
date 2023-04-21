    private static double[] doRetrieve(File cacheFile) throws Exception {
        if (!cacheFile.canRead()) return null;
        double[] cached = new double[(int) (cacheFile.length() / 8)];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            int offset = Mmap.doubles(cached, 0, ch);
            while (offset < cached.length) {
                offset = Mmap.doubles(cached, offset, ch);
            }
        } finally {
            f.close();
        }
        return cached;
    }
