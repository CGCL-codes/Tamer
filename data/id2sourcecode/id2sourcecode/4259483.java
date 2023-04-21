        public static MyFileLock tryLockTempFile(File lockFile, long intervalTime, long timeOut) throws IOException {
            if (!lockFile.isFile()) return null;
            FileLock fl = null;
            long fileSize = lockFile.length();
            long checkTime = System.currentTimeMillis();
            String fileLockPath = lockFile.getAbsolutePath() + ".lock";
            File fileLockFile = FileUtil.createNewFile(fileLockPath, false);
            if (!fileLockFile.isFile()) return null;
            RandomAccessFile raf = new RandomAccessFile(fileLockPath, "rw");
            do {
                fl = raf.getChannel().tryLock();
                if (fl == null) {
                    try {
                        Thread.sleep(intervalTime);
                    } catch (InterruptedException e) {
                        LoaderLogUtil.logExceptionTrace(ShareConstants.shareLogger, Level.WARNING, e);
                        return null;
                    }
                    if (lockFile.isFile()) {
                        long newFileSize = lockFile.length();
                        if (newFileSize == fileSize) {
                            if (System.currentTimeMillis() - checkTime > timeOut) {
                                break;
                            }
                        } else {
                            fileSize = newFileSize;
                            checkTime = System.currentTimeMillis();
                        }
                    } else {
                        break;
                    }
                }
            } while (fl == null);
            if (fl == null) {
                IOUtil.close(raf);
                return null;
            } else {
                MyFileLock mfl = new MyFileLock(raf, fl, fileLockFile);
                return mfl;
            }
        }
