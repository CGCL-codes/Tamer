    @Override
    protected boolean processOptions(String args[], CommandLine cmd) {
        try {
            getController().init(cmd.hasOption(TEST_OPTION));
        } catch (ConfigException e) {
            fatal(e);
            return false;
        }
        extensions = new HashSet<String>();
        try {
            for (File mediaDirLoc : getController().getMediaDirectories()) {
                MediaDirectory mediaDir = getController().getMediaDirectory(mediaDirLoc);
                extensions.addAll(mediaDir.getMediaDirConfig().getExtensions());
            }
        } catch (ConfigException e) {
            log.error("Unable to read configuration", e);
            return false;
        }
        files = new ArrayList<File>();
        for (WatchDirConfig c : getController().getWatchDirectories()) {
            File f = c.getWatchDir();
            if (f.isDirectory()) {
                for (File f2 : FileHelper.listFiles(f)) {
                    if (isAllowedMediaFileType(f2)) {
                        files.add(f2);
                    }
                }
            } else {
                files.add(f);
            }
        }
        if (files.size() > 0) {
            log.info(MessageFormat.format("Found {0} media files...", files.size()));
        } else {
            log.info("Unable to find any media files");
            return false;
        }
        if (cmd.hasOption(NOUPDATE_OPTION)) {
            xbmcUpdate = false;
        }
        if (cmd.hasOption(USE_DEFAULT_OPTION)) {
            useDefaults = false;
        }
        if (cmd.hasOption(DELETE_NON_MEDIA_OPTION)) {
            deleteNonMediaFiles = true;
        }
        if (cmd.hasOption(ACTIONS_OPTION)) {
            doActions = true;
        }
        return true;
    }
