    public static String[] fileRead(URL url) {
        try {
            return FileUtil.fileRead(url.openStream());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
