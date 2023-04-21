    public static HttpURLConnection connect(String pUrl, boolean open) throws IOException {
        URL url = new URL(pUrl);
        HttpURLConnection uc = null;
        if (HTTPFactory.getHTTPProxy() == null) {
            uc = (HttpURLConnection) url.openConnection();
        } else {
            uc = (HttpURLConnection) url.openConnection(HTTPFactory.getHTTPProxy());
        }
        if (open) {
            uc.connect();
        }
        return uc;
    }
