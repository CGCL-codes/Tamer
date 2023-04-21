    public WebFile(URL url, String userAgentString, int timeoutMillis) throws IOException, java.net.MalformedURLException {
        try {
            final java.net.URLConnection uconn = url.openConnection();
            final java.net.HttpURLConnection conn = (java.net.HttpURLConnection) uconn;
            conn.setConnectTimeout(timeoutMillis);
            conn.setReadTimeout(timeoutMillis);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-agent", userAgentString);
            conn.connect();
            responseHeader = conn.getHeaderFields();
            responseCode = conn.getResponseCode();
            responseURL = conn.getURL();
            final int length = conn.getContentLength();
            final String type = conn.getContentType();
            if (type != null) {
                final String[] parts = type.split(";");
                MIMEtype = parts[0].trim();
                for (int i = 1; i < parts.length && charset == null; i++) {
                    final String t = parts[i].trim();
                    final int index = t.toLowerCase().indexOf("charset=");
                    if (index != -1) {
                        charset = t.substring(index + 8);
                    }
                }
            }
            java.io.InputStream stream = conn.getErrorStream();
            if (stream == null) {
                stream = conn.getInputStream();
            }
            content = readStream(length, stream);
            conn.disconnect();
        } catch (ClassCastException ex) {
            throw new MalformedURLException("Url provided '" + url + "' didn't use " + "http/https protocol");
        }
    }
