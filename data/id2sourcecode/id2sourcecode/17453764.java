    public static Reader urlToReader(URL url) throws IOException {
        URLConnection con = url.openConnection();
        return new InputStreamReader(con.getInputStream());
    }
