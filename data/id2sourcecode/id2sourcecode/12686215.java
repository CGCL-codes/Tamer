    public static void copyDirectoryStructure(File sourceDirectory, File destinationDirectory) throws IOException {
        if (!sourceDirectory.exists()) {
            throw new IOException("Source directory doesn't exists (" + sourceDirectory.getAbsolutePath() + ").");
        }
        File[] files = sourceDirectory.listFiles();
        String sourcePath = sourceDirectory.getAbsolutePath();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String dest = file.getAbsolutePath();
            dest = dest.substring(sourcePath.length() + 1);
            File destination = new File(destinationDirectory, dest);
            if (file.isFile()) {
                destination = destination.getParentFile();
                FileUtils.copyFileToDirectory(file, destination);
            } else if (file.isDirectory()) {
                if (!destination.exists() && !destination.mkdirs()) {
                    throw new IOException("Could not create destination directory '" + destination.getAbsolutePath() + "'.");
                }
                copyDirectoryStructure(file, destination);
            } else {
                throw new IOException("Unknown file type: " + file.getAbsolutePath());
            }
        }
    }
