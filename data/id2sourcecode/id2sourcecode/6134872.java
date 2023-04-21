    private void convertDataFile(File f) throws IOException {
        File convertedFile = new File(f.getAbsolutePath() + ".converted");
        FileUtils.copyFile(f, System.getProperty("file.encoding"), convertedFile, "UTF-8");
        FileUtils.renameFile(convertedFile, f);
    }
