    public Reader read(String target) throws IOException {
        URL url = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        String contentType = conn.getHeaderField("Content-Type");
        String encoding = getEncoding(contentType);
        InputStreamReader in = new InputStreamReader(conn.getInputStream(), Charset.forName(encoding));
        return in;
    }
