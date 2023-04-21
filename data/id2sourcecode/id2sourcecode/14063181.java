    private void waitForUpdate(Process psFile, File fp, long modifyDate, String sID, String sVer, String sExt) throws InterruptedException, IOException {
        if (m_sWaitType.equalsIgnoreCase(WTYPE_PROCESS)) psFile.waitFor();
        if (m_sWaitType.equalsIgnoreCase(WTYPE_FILE)) {
            while (true) {
                RandomAccessFile fi = null;
                try {
                    fi = new RandomAccessFile(fp, "rw");
                    FileChannel fc = fi.getChannel();
                    if (fc != null) {
                        FileLock flock = fc.tryLock();
                        if (flock != null) {
                            flock.release();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fi != null) fi.close();
                }
            }
        }
        long newModifyDate = fp.lastModified();
        if (newModifyDate > modifyDate) {
            updateFile(sID, sVer, sExt, fp);
        }
    }
