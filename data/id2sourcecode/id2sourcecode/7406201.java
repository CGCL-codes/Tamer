    private static void unlockFile(File file) {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            FileChannel channel = raf.getChannel();
            try {
                if (channel.tryLock() == null) {
                    log.warn("File " + file.getAbsolutePath() + " is locked and couldn't be released");
                    return;
                }
            } finally {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Couldn't unlock " + file, e);
        }
    }
