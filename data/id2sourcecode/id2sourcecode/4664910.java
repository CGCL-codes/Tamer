    FileLock lock(String id) {
        String lockFileName = fileLockDir + "/" + id + ".lock.tmp";
        FileOutputStream ostream = null;
        FileLock fileLock = null;
        try {
            ostream = new FileOutputStream(lockFileName);
            ostreamContext.set(ostream);
            fileLock = ostream.getChannel().lock();
            return fileLock;
        } catch (Throwable err) {
            LOG.error("Failed to get lock " + lockFileName, err);
            return null;
        } finally {
            if (fileLock == null) IOUtils.closeQuietly(ostream);
        }
    }
