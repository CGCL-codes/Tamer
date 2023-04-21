    @Override
    public String get(String doc, Map<String, String> getData) throws IOException {
        String urlParams = doc + "?";
        for (Map.Entry<String, String> entry : getData.entrySet()) {
            urlParams += entry.getKey() + "=" + entry.getValue() + "&";
        }
        URL url = new URL("http://" + host + ":" + port + "/" + urlParams);
        URLConnection conn = url.openConnection();
        conn.connect();
        return getStringFromInputStream(conn.getInputStream());
    }
