    public static void appendFiles(File destination, List<File> files) throws IOException {
        FileUtils.copyFile(files.get(0), destination);
        FileOutputStream fos = new FileOutputStream(destination, true);
        for (int i = 1; i < files.size(); i++) {
            FileInputStream fis = new FileInputStream(files.get(i));
            IOUtils.copy(fis, fos);
            IOUtils.closeQuietly(fis);
        }
        IOUtils.closeQuietly(fos);
    }
