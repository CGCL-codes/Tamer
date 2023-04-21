    public static void copyDir(File sourceDir, File destinationDir) throws IOException {
        if (sourceDir == null) {
            throw new NullPointerException("sourceDir");
        }
        if (destinationDir == null) {
            throw new NullPointerException("toDir");
        }
        if ((!sourceDir.exists()) || (!destinationDir.exists())) {
            throw new IllegalArgumentException(sourceDir.getPath() + " or " + destinationDir.getPath() + " doesn't exist");
        }
        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException(sourceDir.getPath() + " is not a directory");
        }
        if (!destinationDir.isDirectory()) {
            throw new IllegalArgumentException(destinationDir.getPath() + " is not a directory");
        }
        File[] sourceFiles = sourceDir.listFiles();
        for (int i = 0; i < sourceFiles.length; i++) {
            if (!sourceFiles[i].isDirectory()) {
                copy(sourceFiles[i], destinationDir);
            }
        }
    }
