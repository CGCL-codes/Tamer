    private static void copyWebFiles(File sourceDir) throws ConfigurationException {
        File webdir = new File(sourceDir, WEB_RESOURCES_LOCATION);
        if (!webdir.exists()) {
            return;
        }
        Collection c = FileUtils.listFiles(webdir, null, true);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            try {
                FileUtils.copyFileToDirectory(((File) i.next()), getWebResourcesDir());
            } catch (IOException e) {
                throw new ConfigurationException(logger, "Error copy user web files", e);
            }
        }
    }
