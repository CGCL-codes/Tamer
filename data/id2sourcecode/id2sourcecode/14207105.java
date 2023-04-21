    private boolean checkAvailability() throws IOException {
        if (readCount > file.length()) {
            logger.debug("File " + file.getAbsolutePath() + " is not that long!: " + readCount);
            return false;
        }
        int c = 0;
        long writeCount = file.length();
        long wait = firstRead ? waitSize : 100000;
        while (writeCount - readCount <= wait && c < 15) {
            if (c == 0) {
                logger.trace("Suspend File Read: readCount=" + readCount + " / writeCount=" + writeCount);
            }
            c++;
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
            }
            writeCount = file.length();
        }
        if (c > 0) {
            logger.trace("Resume Read: readCount=" + readCount + " / writeCount=" + file.length());
        }
        return true;
    }
