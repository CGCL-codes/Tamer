    public InputStream getStream(String uri) {
        URL url = null;
        try {
            url = new URL(uri);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        }
        conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
        try {
            return conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
