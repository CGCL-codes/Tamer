    public static String doPost(String spec, String params) throws IOException {
        URL url = new URL(spec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        OutputStream ostream = conn.getOutputStream();
        try {
            IOUtils.write(params, ostream);
            ostream.flush();
        } finally {
            IOUtils.closeQuietly(ostream);
        }
        InputStream istream = conn.getInputStream();
        try {
            return IOUtils.toString(istream);
        } finally {
            IOUtils.closeQuietly(istream);
        }
    }
