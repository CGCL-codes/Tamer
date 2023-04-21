    public static boolean fileCopy(File sourceFile, File outputFile) throws IOException, Exception {
        log.info("Copying file " + sourceFile.getCanonicalPath() + " to " + outputFile.getCanonicalPath());
        return writeBinaryFile(readBinaryFile(sourceFile), outputFile);
    }
