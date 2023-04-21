    public boolean lock(File file, boolean block) {
        boolean flag = false;
        RandomAccessFile raf = null;
        logger.info("Getting lock on " + file.getAbsolutePath());
        try {
            raf = new RandomAccessFile(file, "rw");
            if (block) {
                raf.getChannel().lock();
                flag = true;
            } else {
                if (null != raf.getChannel().tryLock()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        if (flag) {
            logger.info("Got lock!");
        }
        return flag;
    }
