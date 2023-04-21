    private static void createTargetFiles(List<JarFile> jars, String targetDir, boolean client) throws Exception {
        String sep = FileSystemUtil.getFilePathSeparator();
        for (JarFile jar : jars) {
            if (jar.getSourceFile() != null) {
                File targetFile = new File(targetDir + sep + jar.getServer() + sep + jar.getLibraryFile());
                if (client) {
                    targetFile = new File(targetDir + sep + jar.getClient() + sep + jar.getLibraryFile());
                }
                if (!targetFile.exists() || jar.getSourceFile().length() != targetFile.length()) {
                    FileUtils.copyFile(jar.getSourceFile(), targetFile);
                }
            }
        }
    }
