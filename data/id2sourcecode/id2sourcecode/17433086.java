    private HttpURLConnection getNetHead(String url) throws IOException {
        HttpURLConnection netCopy = null;
        HttpURLConnection.setFollowRedirects(true);
        final String HeadRequestMethod = "HEAD";
        try {
            netCopy = (HttpURLConnection) new URL(url).openConnection();
            if (Cache.Debugging) {
                factory.report("got net head for " + url);
            }
        } catch (Exception e) {
            factory.report("unable to get net head for " + url + ": " + e);
        }
        netCopy.setRequestMethod(HeadRequestMethod);
        netCopy.connect();
        if (Cache.Debugging) {
        }
        return netCopy;
    }
