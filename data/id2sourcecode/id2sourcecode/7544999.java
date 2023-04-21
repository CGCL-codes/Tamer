    public static String getContent(URL url) throws IOException {
        if (!ProtocolSet.contains(url.getProtocol())) throw new java.lang.IllegalArgumentException("Non supported protocol");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-agent", "Opera/10.50 (Windows NT 6.1; U; en-GB) Presto/2.2.2");
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
                if (index != -1) charset = t.substring(index + 8);
            }
        }
        final InputStream stream = conn.getErrorStream();
        if (stream != null) content = readStream(length, stream); else if ((content = conn.getContent()) != null && content instanceof java.io.InputStream) content = readStream(length, (java.io.InputStream) content);
        conn.disconnect();
        return String.valueOf(content);
    }
