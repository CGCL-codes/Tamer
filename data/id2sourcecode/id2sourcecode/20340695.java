    public static String doGet(String spec) throws IOException {
        URL url = new URL(spec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        InputStream istream = conn.getInputStream();
        try {
            return IOUtils.toString(istream);
        } finally {
            IOUtils.closeQuietly(istream);
        }
    }
