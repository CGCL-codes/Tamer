    private File createTempCopy(FileAnticipator fa, File sourceDir, File destDir, String file) throws Exception {
        FileUtils.copyFile(new File(sourceDir, file), new File(destDir, file));
        return fa.expecting(destDir, file);
    }
