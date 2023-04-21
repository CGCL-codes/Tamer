    public static Boolean validate(String url) throws Exception {
        try {
            if (url.startsWith("https")) return true;
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            } else {
                Logger myLogger;
                myLogger = Logger.getLogger("");
                myLogger.info("validate " + con.getContentType() + " length: " + con.getContentLength());
                if (con.getContentLength() <= 0) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
