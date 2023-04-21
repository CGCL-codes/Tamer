    public HttpURLConnection getUrlConnection(URL url) throws Exception {
        if (!url.getProtocol().equalsIgnoreCase("http")) throw new Exception("URL does not use the HTTP protocol");
        return (HttpURLConnection) url.openConnection();
    }
