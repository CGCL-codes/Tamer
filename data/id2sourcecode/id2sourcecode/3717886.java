    private static boolean putOrPost(String urlString, String content, String user, String pass, boolean robustMode, boolean isPut) {
        URLConnection conn = null;
        try {
            conn = openConnection(urlString, user, pass, isPut ? PUT : POST, content, robustMode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) LOGGER.debug("response of HTTP " + (isPut ? "PUT: " : "POST: ") + convertStreamToString(conn.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }
