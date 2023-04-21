    public static boolean lock() {
        try {
            String tmpdir = System.getProperty("java.io.tmpdir");
            mFile = new File(tmpdir + File.separator + "ctsLockFile.txt");
            mFileOs = new FileOutputStream(mFile);
            mLock = mFileOs.getChannel().tryLock();
            if (mLock != null) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e1) {
            return false;
        } catch (IOException e1) {
            return false;
        }
    }
