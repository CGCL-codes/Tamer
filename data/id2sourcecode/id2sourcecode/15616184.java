    public static File extractFile(String srcFile, String toDir, String destFilename, Class cls) {
        URL url = cls.getResource(srcFile);
        URLConnection connection = null;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        File dir = new File(toDir);
        if (!dir.exists()) dir.mkdirs();
        File cache = new File(toDir, destFilename);
        return extractFile(connection, cache);
    }
