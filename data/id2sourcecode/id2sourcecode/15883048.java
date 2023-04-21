    public static void setHomeDirectory(String pathname) {
        File mh = new File(pathname);
        if (!mh.exists()) {
            log.error("Error - the specified home directory does not exist (" + pathname + ")");
        } else if (!mh.canRead() || !mh.canWrite()) {
            log.error("Error - the user running this application can not read " + "and write to the specified home directory (" + pathname + "). " + "Please grant the executing user read and write permissions.");
        } else {
            HOME_FOLDER = pathname;
        }
    }
