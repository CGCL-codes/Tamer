    public static Vector parse(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return parse(reader);
    }
