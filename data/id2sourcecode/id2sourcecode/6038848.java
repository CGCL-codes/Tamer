    public static boolean isOnline(URL url) {
        InputStream is = null;
        boolean isOnline = false;
        try {
            is = url.openStream();
            isOnline = is != null;
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return isOnline;
    }
