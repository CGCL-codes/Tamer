    public static boolean urlExists(URL url) {
        boolean v = false;
        if (null != url) {
            try {
                URLConnection uc = url.openConnection();
                v = true;
            } catch (IOException ioe) {
            }
        }
        return v;
    }
