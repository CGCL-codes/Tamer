    private String getResponseFromURL(String url) throws IOException {
        URLConnection c = new URL(url).openConnection();
        InputStream in = c.getInputStream();
        StringBuffer result = new StringBuffer();
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = r.readLine()) != null) result.append(line);
        line = result.toString();
        return line;
    }
