    public static void deployXar(final String deployDir, final String zipFilename) throws IOException {
        final File newZipFile = new File(zipFilename);
        final File deployDirFile = new File(deployDir + "/" + newZipFile.getName());
        FileUtils.copyFile(newZipFile, deployDirFile);
    }
