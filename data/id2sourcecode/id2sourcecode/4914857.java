    public static void webXml(String fromFile, String toFile) throws Exception {
        FileUtils.copyFile(new File(fromFile), new File(toFile));
    }
