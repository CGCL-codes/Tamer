    protected static String getUrlContentsAsString(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuffer result = new StringBuffer();
        while ((line = br.readLine()) != null) {
            result.append(line + "\r\n");
        }
        br.close();
        return result.toString();
    }
