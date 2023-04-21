    private static boolean isGdOpen() {
        File file = new File(Constants.CURRENT_DIR + ".gd");
        file.delete();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
            }
            try {
                FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
                try {
                    channel.tryLock();
                } catch (OverlappingFileLockException e) {
                }
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }
