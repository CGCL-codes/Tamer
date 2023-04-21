    private static String createSHA1String(final File file) throws IOException, InterruptedException {
        MessageDigest md = new SHA1();
        byte[] buffer = new byte[65536];
        int read;
        IntWrapper progress = new IntWrapper(0);
        progressMap.put(file, progress);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            while ((read = fis.read(buffer)) != -1) {
                long start = System.currentTimeMillis();
                md.update(buffer, 0, read);
                progress.addInt(read);
                if (SystemUtils.getIdleTime() < MIN_IDLE_TIME) {
                    long end = System.currentTimeMillis();
                    long interval = end - start;
                    if (interval > 0) Thread.sleep(interval * 3); else Thread.yield();
                }
            }
        } finally {
            progressMap.remove(file);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
        byte[] sha1 = md.digest();
        return UrnType.URN_NAMESPACE_ID + UrnType.SHA1_STRING + Base32.encode(sha1);
    }
