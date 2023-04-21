    public synchronized boolean lock() throws IOException {
        fileStream = new FileOutputStream(lockFile, true);
        try {
            fileLock = fileStream.getChannel().tryLock();
        } catch (IOException ioe) {
            if (BasicLocation.DEBUG) {
                String basicMessage = NLS.bind(EclipseAdaptorMsg.location_cannotLock, lockFile);
                FrameworkLogEntry basicEntry = new FrameworkLogEntry(FrameworkAdaptor.FRAMEWORK_SYMBOLICNAME, basicMessage, 0, ioe, null);
                EclipseAdaptor.getDefault().getFrameworkLog().log(basicEntry);
            }
            String specificMessage = NLS.bind(EclipseAdaptorMsg.location_cannotLockNIO, new Object[] { lockFile, ioe.getMessage(), "\"-D" + BasicLocation.PROP_OSGI_LOCKING + "=none\"" });
            throw new IOException(specificMessage);
        }
        if (fileLock != null) return true;
        fileStream.close();
        fileStream = null;
        return false;
    }
