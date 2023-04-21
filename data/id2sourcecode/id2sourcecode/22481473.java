    public void snapshotToLaunchDir(File readFile) throws IOException {
        if (appCtx.getCurrentLaunchDir() == null || !appCtx.getCurrentLaunchDir().exists()) {
            logger.log(Level.WARNING, "launch directory unavailable to snapshot " + readFile);
            return;
        }
        FileUtils.copyFileToDirectory(readFile, appCtx.getCurrentLaunchDir());
    }
