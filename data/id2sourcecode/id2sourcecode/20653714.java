    protected void initLaunchDir() {
        initLaunchId();
        try {
            currentLaunchDir = new File(getConfigurationFile().getParentFile(), getCurrentLaunchId());
            if (!currentLaunchDir.mkdir()) {
                throw new IOException("failed to create directory " + currentLaunchDir);
            }
            FileUtils.copyFileToDirectory(getConfigurationFile(), currentLaunchDir);
            File latestSymlink = new File(getConfigurationFile().getParentFile(), "latest");
            latestSymlink.delete();
            boolean success = FilesystemLinkMaker.makeSymbolicLink(currentLaunchDir.getName(), latestSymlink.getPath());
            if (!success) {
                LOGGER.warning("failed to create symlink from " + latestSymlink + " to " + currentLaunchDir);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to initialize launch directory: " + e);
            currentLaunchDir = null;
        }
    }
