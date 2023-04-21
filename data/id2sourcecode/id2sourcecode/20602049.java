    private static boolean copy(File file) {
        String srcName = PersistenceManager.getInstance().getDatabaseName();
        String srcFileName = srcName + "." + DB_FILE_ENDING;
        File srcFile = new File(srcFileName);
        try {
            FileUtils.copyFile(srcFile, file);
        } catch (IOException e) {
            logger.error(e.getMessage());
            SwingTools.showException(e);
            return false;
        }
        return true;
    }
