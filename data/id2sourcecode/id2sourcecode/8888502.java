    @Override
    public void setURL(String spec) throws MalformedURLException, IOException {
        final URL url = new URL(spec);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(requestMethod.toString());
        conn.setDoOutput(true);
        conn.connect();
    }
