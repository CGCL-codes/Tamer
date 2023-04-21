    private final void copyFileByProperties(File namedCfgPath, String propertiesFileName, String prefix, File packagePath) throws IOException {
        File resourceFile = copyFile(namedCfgPath, propertiesFileName, prefix, null);
        if (resourceFile.exists()) {
            Properties p = new Properties();
            p.load(new FileInputStream(resourceFile));
            for (Object key : p.keySet()) {
                String targetPath = (String) key;
                String sourcePath = (String) p.get(key);
                File sourceFile = sourcePath.startsWith("$") ? new File(manager.getHomePath(), sourcePath.substring(1)) : new File(sourcePath);
                File targetFile = targetPath.startsWith("\\.") ? packagePath : new File(packagePath, targetPath.split("\\.")[0]);
                if (sourceFile.exists()) {
                    if (sourceFile.isDirectory()) FileUtils.copyDirectory(sourceFile, targetFile, FileFilterUtils.makeSVNAware(null)); else FileUtils.copyFileToDirectory(sourceFile, targetFile);
                }
            }
        }
    }
