    @Override
    public int read(boolean firstRead, long readCount) {
        if (readCount > INITIAL_BUFFER_SIZE && readCount < maxMemorySize) {
            int newMargin = maxMemorySize - MARGIN_MEDIUM;
            if (bufferOverflowWarning != newMargin) {
                logger.debug("Setting margin to 2Mb");
            }
            this.bufferOverflowWarning = newMargin;
        }
        if (eof && readCount >= writeCount) {
            return -1;
        }
        int c = 0;
        int minBufferS = firstRead ? minMemorySize : secondread_minsize;
        while (writeCount - readCount <= minBufferS && !eof && c < 15) {
            if (c == 0) {
                logger.trace("Suspend Read: readCount=" + readCount + " / writeCount=" + writeCount);
            }
            c++;
            try {
                Thread.sleep(CHECK_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
        if (attachedThread != null) {
            attachedThread.setReadyToStop(false);
        }
        if (c > 0) {
            logger.trace("Resume Read: readCount=" + readCount + " / writeCount=" + writeCount);
        }
        if (buffer == null || !buffered) {
            return -1;
        }
        return 0xff & buffer[(int) (readCount % maxMemorySize)];
    }
