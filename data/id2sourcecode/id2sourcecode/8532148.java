    public static void copy(java.io.File fromFile, java.io.File toFile) throws IOException {
        FileUtils.write(FileUtils.readAll(fromFile), toFile);
    }
