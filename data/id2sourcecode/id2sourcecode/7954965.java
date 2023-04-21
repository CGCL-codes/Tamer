    private void checkLogFolderFiles() {
        boolean logfolder_created = false;
        boolean logfile_renamed = false;
        if (options.getLog_file_folder().isDirectory() == false) {
            options.getLog_file_folder().mkdirs();
            logfolder_created = true;
        }
        if (options.getLog_file().exists()) {
            logfile_renamed = true;
            options.getLog_file().renameTo(new File(options.getLog_file() + ".old"));
        }
        if (logfolder_created) {
            logger.writeLog(JammEnum.WARNING, "Jamm Logfolder not existing");
            logger.writeLog(JammEnum.INFO, "Jamm Logfolder created");
        }
        if (logfile_renamed) {
            logger.writeLog(JammEnum.WARNING, "Logfile already existing");
            logger.writeLog(JammEnum.INFO, "Old Logfile renamed to *.log.bak");
        }
    }
