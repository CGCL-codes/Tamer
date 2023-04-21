    public static URLConnection openConnection(String urlString, String user, String pass, boolean robustMode) throws MalformedURLException, IOException {
        return openConnection(urlString, user, pass, GET, null, robustMode);
    }
