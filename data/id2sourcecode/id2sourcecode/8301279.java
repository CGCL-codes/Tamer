    public static byte[] loadUrlRaw(URL url) {
        try {
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            return loadInputStream(in);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
