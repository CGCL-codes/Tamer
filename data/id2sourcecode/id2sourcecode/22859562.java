    private void copySutToTestsFolder(File sutFile) throws IOException {
        File classDir = new File(JSystemProperties.getCurrentTestsPath());
        if (sutFile.getAbsolutePath().startsWith(classDir.getAbsolutePath())) {
            File sutSrcFile = new File(JSystemProperties.getInstance().getPreference(FrameworkOptions.TESTS_SOURCE_FOLDER) + sutFile.getAbsolutePath().substring(classDir.getAbsolutePath().length()));
            FileUtils.copyFile(sutFile, sutSrcFile);
        }
    }
