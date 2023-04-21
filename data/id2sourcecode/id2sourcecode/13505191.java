    protected static void copyFile(File dir, String src, String dest) throws IOException {
        FileUtils.copyFile(openFile(src), new File(dir, dest));
    }
