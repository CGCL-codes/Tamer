    @Override
    protected boolean processOptions(String args[], CommandLine cmd) {
        rootMediaDir = null;
        if (cmd.hasOption(ROOT_MEDIA_DIR_OPTION) && cmd.getOptionValue(ROOT_MEDIA_DIR_OPTION) != null) {
            File dir = new File(cmd.getOptionValue(ROOT_MEDIA_DIR_OPTION));
            if (dir.isDirectory()) {
                try {
                    getController().init(cmd.hasOption(TEST_OPTION));
                    rootMediaDir = getController().getMediaDirectory(dir);
                } catch (ConfigException e) {
                    fatal(e);
                    return false;
                }
            } else {
                fatal(MessageFormat.format(Messages.getString("CLICopyStoreToStore.MEDIA_DIR_NOT_WRITABLE"), dir));
                return false;
            }
            if (rootMediaDir == null || !rootMediaDir.getMediaDirConfig().getMediaDir().exists()) {
                fatal(MessageFormat.format(Messages.getString("CLICopyStoreToStore.MEDIA_DIR_DOES_NOT_EXIST"), dir));
                return false;
            }
        }
        fromStore = findStoreById(cmd.getOptionValue(FROM_STORE_OPTION));
        if (fromStore == null) {
            return false;
        }
        toStore = findStoreById(cmd.getOptionValue(TO_STORE_OPTION));
        if (fromStore == null) {
            return false;
        }
        if (cmd.getArgs().length == 0) {
            fatal(Messages.getString("CLICopyStoreToStore.MISSING_ARG"));
            return false;
        } else {
            files = new ArrayList<File>();
            for (String s : cmd.getArgs()) {
                File file = null;
                if (s.startsWith(File.separator)) {
                    file = new File(s);
                } else {
                    file = new File(FileHelper.getWorkingDirectory(), s);
                }
                if (!file.exists()) {
                    fatal(MessageFormat.format(Messages.getString("CLICopyStoreToStore.UNABLE_FIND_FILE"), file));
                    return false;
                }
                files.add(file);
            }
        }
        if (cmd.hasOption(NOUPDATE_OPTION)) {
            xbmcUpdate = false;
        }
        return true;
    }
