    public static boolean checkURLStatus(String pURL) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(pURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            conn.disconnect();
            conn = null;
            url = null;
        }
    }
