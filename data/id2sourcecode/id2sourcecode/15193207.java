    @Override
    public void openImpl() throws FileIOException {
        int tryCount = 0;
        boolean done = false;
        long startTime = System.currentTimeMillis();
        while (!done) {
            final String message = "failed open " + file;
            try {
                randomAccessFile = new RandomAccessFile(file, MODE_RWS);
                done = true;
                if (tryCount != 0) {
                    logger.warn("succeed open after " + tryCount + " try and " + (System.currentTimeMillis() - startTime) + M_S);
                }
            } catch (Exception exception) {
                if (++tryCount >= maxRetry) {
                    final String failureMessage = message + " try " + tryCount + " time, on " + (System.currentTimeMillis() - startTime) + M_S;
                    logger.fatal(failureMessage);
                    throw HELPER_FILE_UTIL.fileIOException(failureMessage, file, exception);
                }
                logger.error("failed open " + file);
                try {
                    Thread.sleep(retryMsDelay);
                } catch (InterruptedException exception2) {
                    logger.error(message);
                    throw HELPER_FILE_UTIL.fileIOException(WAIT_INTERRUPTED + message, file, exception);
                }
            }
        }
        tryCount = 0;
        done = false;
        startTime = System.currentTimeMillis();
        while (!done) {
            try {
                fileLock = randomAccessFile.getChannel().lock();
                if (fileLock == null) {
                    throw new IOException("no lock");
                } else {
                    done = true;
                    if (tryCount != 0) {
                        logger.warn("succeed look after " + tryCount + " try and " + (System.currentTimeMillis() - startTime) + M_S);
                    }
                }
            } catch (Exception exception) {
                final String message = "failed lock " + file;
                if (++tryCount >= maxRetry) {
                    try {
                        randomAccessFile.close();
                    } catch (Exception exception2) {
                        logger.warn("while closing after acquire lock failure", exception2);
                    }
                    final String failureMessage = message + " try " + tryCount + " time, on " + (System.currentTimeMillis() - startTime) + M_S;
                    logger.fatal(failureMessage, exception);
                    randomAccessFile = null;
                    throw HELPER_FILE_UTIL.fileIOException(failureMessage, file, exception);
                }
                logger.error(message, exception);
                try {
                    Thread.sleep(retryMsDelay);
                } catch (InterruptedException exception2) {
                    logger.error(message, exception2);
                    throw HELPER_FILE_UTIL.fileIOException(WAIT_INTERRUPTED + message, file, exception);
                }
            }
        }
        currentPositionInFile = 0;
        if (logger.debugEnabled) {
            logger.debug("open " + file);
        }
        fileChannel = randomAccessFile.getChannel();
    }
