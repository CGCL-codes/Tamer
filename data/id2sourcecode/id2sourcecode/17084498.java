    boolean validateLink(String link) {
        try {
            URL url = new URL(link);
            URLConnection conn = (URLConnection) url.openConnection();
            if (conn instanceof HttpURLConnection) {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.connect();
                int respCode = httpConn.getResponseCode();
                if (respCode < 200 || respCode >= 400) {
                    return false;
                }
            }
        } catch (MalformedURLException mue) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }
