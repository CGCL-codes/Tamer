    private String getRealContents(HttpServletRequest req, String uri) throws IOException {
        if (realContentsCache.containsKey(uri)) {
            return realContentsCache.get(uri);
        }
        String host = req.getLocalName();
        int port = req.getLocalPort();
        String realUrl = "http://" + host + ":" + port + uri;
        debug("Fetching: " + realUrl);
        URL url = new URL(realUrl);
        InputStream is = url.openStream();
        String data = Util.readStreamAsString(is);
        is.close();
        realContentsCache.put(uri, data);
        return data;
    }
