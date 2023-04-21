    public synchronized int cleanCache(float maxAge) {
        long expireDate = System.currentTimeMillis() - (long) (maxAge * 60.0f * 60.0f * 1000.0f);
        File basedir = new File(m_rfsRepository);
        int count = 0;
        if (basedir.canRead() && basedir.isDirectory()) {
            File[] files = basedir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    if (f.canWrite()) {
                        if (f.lastModified() < expireDate) {
                            try {
                                f.delete();
                                count++;
                            } catch (Exception e) {
                                if (LOG.isWarnEnabled()) {
                                    LOG.warn(Messages.get().getBundle().key(Messages.LOG_EXCERPT_CACHE_DELETE_ERROR_1, f.getAbsolutePath()), e);
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
