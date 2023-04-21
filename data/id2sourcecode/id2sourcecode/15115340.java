    private static byte[] doRetrieveNew(File cacheFile) throws Exception {
        byte[] cached = new byte[(int) cacheFile.length()];
        FileInputStream f = new FileInputStream(cacheFile);
        try {
            FileChannel ch = f.getChannel();
            Mmap.bytes(cached, 0, ch);
            ch.close();
        } finally {
            f.close();
        }
        return cached;
    }
