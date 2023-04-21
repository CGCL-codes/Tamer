    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String destDirname = commandLine.getOptionValue(CMD_LINE_DESTDIR_OPTION);
        if (destDirname == null) {
            destDirname = new File(getStageDir(), getDescriptor().getId()).getPath();
        }
        File destDir = new File(destDirname);
        destDir.mkdirs();
        if (commandLine.hasOption(CMD_LINE_DEPLOY_API_OPTION)) {
            File jar = getFilePath("mqKeyStore-api.jar");
            logger.debug("Copy " + jar.getPath() + " to " + destDir.getPath());
            FileUtils.copyFileToDirectory(jar, destDir);
        }
        String rarFilename = commandLine.getOptionValue(CMD_LINE_RAR_FILE_OPTION);
        if (rarFilename != null) {
            File sourceRAR = getFilePath("mqKeyStore.rar");
            File destinationRAR = new File(destDir, rarFilename);
            logger.debug("Copy " + sourceRAR.getPath() + " to " + destDir.getPath());
            FileUtils.copyFile(sourceRAR, destinationRAR);
        }
    }
