    @Deprecated
    public static void copyFile(File inputFile, File outputFile, boolean keepLastModified) throws IOException {
        org.codehaus.plexus.util.FileUtils.copyFile(inputFile, outputFile);
        if (keepLastModified) {
            outputFile.setLastModified(inputFile.lastModified());
        }
    }
