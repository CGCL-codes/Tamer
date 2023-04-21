    public static synchronized boolean testHTTPRequestHeader(URL url, String header) throws Exception {
        HttpURLConnection init = (HttpURLConnection) url.openConnection();
        init.setConnectTimeout(READ_TIME_OUT);
        init.setReadTimeout(READ_TIME_OUT);
        init.setUseCaches(false);
        init.setRequestMethod("GET");
        if (init.getHeaderField(header) != null) {
            return true;
        }
        return false;
    }
