    public static boolean exists(URL url) {
        InputStream input = null;
        try {
            URLConnection conn = url.openConnection();
            input = url.openStream();
            if (conn instanceof HttpURLConnection) {
                return ((HttpURLConnection) conn).getResponseCode() == 200;
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException e) {
                ThrowableManagerRegistry.caught(e);
            }
        }
    }
