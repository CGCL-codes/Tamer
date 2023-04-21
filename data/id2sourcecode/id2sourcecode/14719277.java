    public static ReadableCheckResult isReadable(File file) {
        ReadableCheckResult ret = new ReadableCheckResult();
        if (file == null || isDirectory(file)) {
            ret.setReadable(false);
        } else {
            String message = null;
            FileLock lock = null;
            RandomAccessFile raf = null;
            synchronized (FileSystemManager.class) {
                try {
                    raf = new RandomAccessFile(file, "r");
                } catch (Throwable e) {
                    message = e.getMessage();
                }
                if (raf != null) {
                    FileChannel chn = raf.getChannel();
                    try {
                        lock = chn.tryLock(0L, Long.MAX_VALUE, true);
                    } catch (Throwable e) {
                        message = e.getMessage();
                    } finally {
                        if (lock != null && lock.isValid()) {
                            try {
                                lock.release();
                            } catch (IOException ignored) {
                            }
                        }
                        try {
                            raf.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
            if (lock == null) {
                ret.setReadable(false);
                ret.setCause(message);
            } else {
                ret.setReadable(true);
            }
        }
        return ret;
    }
