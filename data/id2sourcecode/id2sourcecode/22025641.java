    private static OutputStream getOutputStream(String uri) throws IOException {
        if (uri.startsWith("file:")) return new FileOutputStream(uri.substring(5));
        URL url = new URL(uri);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        return conn.getOutputStream();
    }
