    public static boolean doesImageExist(URL url) {
        boolean exists = false;
        try {
            InputStream in = url.openStream();
            in.read();
            in.close();
            exists = true;
        } catch (Exception e) {
        }
        return exists;
    }
