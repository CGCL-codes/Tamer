    public static void processResources(IModuleManager manager, File warPath, File classesPath, File libPath) throws IOException {
        File homePath = getHomePath(manager);
        FileUtils.copyDirectory(homePath, classesPath, FileFilterUtils.or(FileFilterUtils.suffixFileFilter("class"), FileFilterUtils.directoryFileFilter()));
        FileUtils.copyFileToDirectory(new File(homePath, "hibernate.eibs.xml"), classesPath);
        FileUtils.copyFileToDirectory(new File(homePath, "oscache.properties"), classesPath);
    }
