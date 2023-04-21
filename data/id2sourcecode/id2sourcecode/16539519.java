    public static File copyFileToDirectory(final File fromFile, final File toDir) throws IOException {
        org.apache.commons.io.FileUtils.copyFileToDirectory(fromFile, toDir);
        return new File(toDir.getAbsolutePath() + File.separator + fromFile.getName());
    }
