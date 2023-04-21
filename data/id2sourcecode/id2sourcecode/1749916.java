    public static boolean RedirectCheck(URL url) {
        boolean retVal = false;
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setInstanceFollowRedirects(false);
            if (con.getResponseCode() == 302) retVal = true; else retVal = false;
            con.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }
