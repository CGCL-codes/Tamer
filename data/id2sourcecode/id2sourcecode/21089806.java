    private static char[] doRetrieve(File cacheFile) throws Exception {
        char[] cached = new char[(int) (cacheFile.length() / N_BYTE)];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            int offset = Mmap.chars(cached, 0, ch);
            if (offset < cached.length) {
                Mmap.chars(cached, offset, ch);
            }
        } finally {
            f.close();
        }
        return cached;
    }
