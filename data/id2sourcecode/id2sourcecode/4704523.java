    public URLConnection createHttpURLConnection(String url) throws MalformedURLException, IOException {
        HttpURLConnection.setFollowRedirects(true);
        if (proxyHost != null && proxyPort > 0) {
            return new HttpURLConnectionViaProxy(new URL(url), proxyHost, proxyPort);
        } else {
            return new URL(url).openConnection();
        }
    }
