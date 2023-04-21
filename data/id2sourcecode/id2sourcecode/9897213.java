    public static void isInstanceRunning() {
        RandomAccessFile f = null;
        FileChannel fc = null;
        FileLock fl = null;
        try {
            File temp = new File(GlobalVar.USER_HOME + GlobalVar.FILE_PATH_DELIMITER + GlobalConstant.DEFAULT_CONFIG_DIR + GlobalVar.FILE_PATH_DELIMITER + GlobalConstant.LOCK);
            File tempFolder = temp.getParentFile();
            if (tempFolder == null || !tempFolder.exists()) {
                tempFolder.mkdir();
            }
            if (temp == null || !temp.exists()) {
                temp.createNewFile();
            }
            f = new RandomAccessFile(GlobalVar.USER_HOME + GlobalVar.FILE_PATH_DELIMITER + GlobalConstant.DEFAULT_CONFIG_DIR + GlobalVar.FILE_PATH_DELIMITER + GlobalConstant.LOCK, "rwd");
            fc = f.getChannel();
            fl = fc.tryLock();
            if (!fl.isValid()) {
                logger.debug("另外一个实例正在运行");
                System.exit(1);
            }
        } catch (Exception e) {
            logger.debug("另外一个实例正在运行");
            System.exit(1);
        }
    }
