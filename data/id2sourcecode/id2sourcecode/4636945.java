    private static boolean deployLibraries(ApplicationSettings applicationSettings, String auxTempRootFolder, String generatorsPackageKey) throws IOException {
        String from = auxTempRootFolder + "/" + ar.com.coonocer.CodingJoy.utils.FileUtils.LIBRARIES_FOLDER;
        if (!new File(from).exists()) {
            logger.info("Source folder for libraries does not exist in installer:" + from);
            return true;
        }
        String appServerTemplatesRootPath = Plugins_App_PIn_M1_EntityHelper.getAppServerTemplatesRootFolder(applicationSettings.getPlugins());
        String to = ar.com.coonocer.CodingJoy.utils.FileUtils.getAbsolutePath(appServerTemplatesRootPath) + "/WEB-INF/lib";
        if (!new File(to).exists()) {
            logger.error("Templates Web Application folder does not exist:" + to);
            return false;
        }
        File[] files = new File(from).listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                File fileTo = new File(to + "/GENLIB_" + generatorsPackageKey + "_" + file.getName());
                FileUtils.copyFile(file, fileTo);
                logger.debug("Copied file " + file + " to " + to);
            }
        }
        return true;
    }
