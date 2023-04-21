    public String sendOfURLConn(String address, String type, String charSet, Proxy proxy) throws IOException {
        StringBuffer context = new StringBuffer();
        HttpURLConnection connection = null;
        URL url = new URL(address);
        if (proxy == null) connection = (HttpURLConnection) url.openConnection(); else connection = (HttpURLConnection) url.openConnection(proxy);
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
        type = type.toUpperCase().trim();
        connection.setRequestMethod(type);
        InputStream in = connection.getInputStream();
        context = this.inputStreamToString(in, charSet);
        connection = null;
        return context.toString();
    }
