    public static File getJAMWikiResourceFile(String filename) throws IOException {
        File resourceFile = null;
        if (Environment.isInitialized()) {
            File resourceDirectory = new File(Environment.getValue(Environment.PROP_BASE_FILE_DIR), RESOURCES_DIR);
            resourceFile = FileUtils.getFile(resourceDirectory, filename);
            if (!resourceFile.exists()) {
                File setupFile = ResourceUtil.getClassLoaderFile(new File(RESOURCES_SETUP_DIR, filename).getPath());
                if (setupFile.exists()) {
                    FileUtils.copyFile(setupFile, resourceFile);
                }
            }
            if (!resourceFile.exists()) {
                throw new FileNotFoundException("Resource file " + filename + " not found in system directory " + resourceDirectory.getAbsolutePath());
            }
        } else {
            resourceFile = ResourceUtil.getClassLoaderFile(new File(RESOURCES_SETUP_DIR, filename).getPath());
            if (!resourceFile.exists()) {
                throw new FileNotFoundException("Resource file " + filename + " not found in system setup resources.");
            }
        }
        return resourceFile;
    }
