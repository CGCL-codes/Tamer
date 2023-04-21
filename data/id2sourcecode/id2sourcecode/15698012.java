    public static void copyFile(String srcFile, String destFile) {
        try {
            FileUtils.copyFile(new File(srcFile), new File(destFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
