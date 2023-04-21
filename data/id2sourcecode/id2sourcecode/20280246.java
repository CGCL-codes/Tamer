    private void init(final String mimeCacheFile) {
        String cacheFile = mimeCacheFile;
        if (!new File(cacheFile).exists()) {
            cacheFile = internalMimeCacheFile;
        }
        FileChannel rCh = null;
        try {
            RandomAccessFile raf = null;
            raf = new RandomAccessFile(cacheFile, "r");
            rCh = (raf).getChannel();
            content = rCh.map(FileChannel.MapMode.READ_ONLY, 0, rCh.size());
            initMimeTypes();
            if (log.isDebugEnabled()) {
                log.debug("Registering a FileWatcher for [" + cacheFile + "]");
            }
            TimerTask task = new FileWatcher(new File(cacheFile)) {

                protected void onChange(File file) {
                    initMimeTypes();
                }
            };
            timer = new Timer();
            timer.schedule(task, new Date(), 10000);
        } catch (Exception e) {
            throw new MimeException(e);
        } finally {
            if (rCh != null) {
                try {
                    rCh.close();
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }
