    public static void moveFile(String fileOrigen, String fileDestiny) throws IOException {
        File originalFile = new File(fileOrigen);
        FileUtils.copyFile(originalFile, new File(fileDestiny));
        originalFile.delete();
    }
