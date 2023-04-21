    private static boolean deployFiles(ApplicationSettings applicationSettings, String auxTempRootFolder, String generatorsPackageKey) throws IOException {
        String from = auxTempRootFolder + "/" + ar.com.coonocer.CodingJoy.utils.FileUtils.TEMPLATES_FOLDER;
        if (!new File(from).exists()) {
            logger.error("Source folder for templates does not exist in installer:" + from);
            return true;
        }
        String appServerTemplatesRootPath = Plugins_App_PIn_M1_EntityHelper.getAppServerTemplatesRootFolder(applicationSettings.getPlugins());
        String to = ar.com.coonocer.CodingJoy.utils.FileUtils.getAbsolutePath(appServerTemplatesRootPath);
        if (!new File(to).exists()) {
            logger.info("Templates Web Application folder does not exist:" + to);
            return false;
        }
        to = to + "/" + ar.com.coonocer.CodingJoy.utils.FileUtils.TEMPLATES_FOLDER + "/" + generatorsPackageKey;
        File fileTo = new File(to);
        if (!fileTo.exists()) {
            if (!fileTo.mkdirs()) {
                logger.error("Destination folder could not be created:" + to);
                return false;
            }
        }
        File[] files = new File(from).listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                FileUtils.copyFileToDirectory(file, fileTo);
                logger.debug("Copied file " + file + " to " + to);
            }
        }
        return true;
    }
