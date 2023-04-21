    public static void makeFileCopy(String filename) throws IOException {
        FileUtils.copyFile(new File(filename), new File(fileCopyName(filename)));
    }
