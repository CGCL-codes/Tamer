    private boolean tryLock() {
        FileOutputStream fo = null;
        File file = new File(".lock");
        try {
            fo = new FileOutputStream(file);
            FileLock lock = fo.getChannel().tryLock();
            if (lock == null) {
                logger.warn("<Warning> <Could not start epayment. this application is still alive.>");
                return false;
            } else {
                return true;
            }
        } catch (FileNotFoundException e) {
            logger.error("checkSftp: lock file can't be created.error:" + e);
            return false;
        } catch (IOException e) {
            logger.error("checkSftp: lock file can't be created.error:" + e);
            return false;
        }
    }
