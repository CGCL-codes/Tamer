    public static void copy(java.io.File fromFile, java.io.File toFile) throws IOException {
        write(readAll(fromFile), toFile);
    }
