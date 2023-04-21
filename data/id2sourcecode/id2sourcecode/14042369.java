    protected static String getFileContents(String filename) {
        try {
            URL url = AutoData.class.getResource(filename);
            URLConnection conn = url.openConnection();
            return new String(TinyWebServer.slurpContents(conn.getInputStream(), true));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
