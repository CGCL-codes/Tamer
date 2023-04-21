    private static int[] doRetrieveMmap(File cacheFile) throws Exception {
        int[] cached = new int[(int) (cacheFile.length() / N_BYTE)];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            int offset = Mmap.ints(cached, 0, ch);
            while (offset < cached.length) {
                offset = Mmap.ints(cached, offset, ch);
            }
            ch.close();
        } finally {
            f.close();
        }
        return cached;
    }
