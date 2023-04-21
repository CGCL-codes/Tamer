    public static void processResources(IModuleManager manager, File warPath, File classesPath, File libPath) throws IOException {
        File resourcePath = new File("resource", ZUL_PAGE_HOME);
        if (resourcePath.exists()) FileUtils.copyDirectory(resourcePath, warPath, FileFilterUtils.makeSVNAware(null));
        FileUtils.copyDirectory(manager.getSourcePath(), classesPath, FileFilterUtils.suffixFileFilter("properties"));
        for (File file : (Collection<File>) FileUtils.listFiles(manager.getSourcePath(), FileFilterUtils.suffixFileFilter("_en_US.properties"), null)) {
            String name = file.getName().replaceAll("_en_US", "");
            FileUtils.copyFile(file, new File(classesPath, name));
        }
        FileUtils.copyDirectory(manager.getSourcePath(), classesPath, FileFilterUtils.suffixFileFilter("ctl"));
        for (File file : (Collection<File>) FileUtils.listFiles(manager.getSourcePath(), FileFilterUtils.suffixFileFilter("_en_US.ctl"), null)) {
            String name = file.getName().replaceAll("_en_US", "");
            FileUtils.copyFile(file, new File(classesPath, name));
        }
        File homePath = getHomePath(manager);
        FileUtils.copyFileToDirectory(new File(homePath, "page.properties"), classesPath);
        FileUtils.copyDirectory(getHomePath(manager), warPath, FileFilterUtils.suffixFileFilter("zul"));
    }
