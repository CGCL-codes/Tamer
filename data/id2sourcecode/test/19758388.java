    private static void cacheXML(URL url, File XMLFromOnline) {
        File cachedFileLocation = new File(getCachedXMLFileName(url));
        if (!cachedFileLocation.getParentFile().exists()) cachedFileLocation.getParentFile().mkdir();
        if (cachedFileLocation.exists()) cachedFileLocation.delete();
        Config.log(DEBUG, "Caching XML from " + url + " to " + cachedFileLocation);
        try {
            cachedFileLocation.createNewFile();
            FileUtils.copyFile(XMLFromOnline, cachedFileLocation);
        } catch (Exception x) {
            Config.log(INFO, "Failed to copy file " + XMLFromOnline + " to " + cachedFileLocation, x);
        }
    }
