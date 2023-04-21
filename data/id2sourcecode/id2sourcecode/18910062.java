    public void copyFileToDirectory(File file, File outputDirectory) {
        try {
            FileUtils.copyFileToDirectory(file, outputDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
