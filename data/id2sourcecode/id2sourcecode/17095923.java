    public static final InputStream openURL(URL source_url) throws IOException, SecurityException {
        if (false) {
            System.out.println("Util::openUrl( " + source_url + " )");
        }
        InputStream input_stream = null;
        URLConnection url_connection = source_url.openConnection();
        url_connection.setUseCaches(true);
        if (url_connection instanceof HttpURLConnection) {
            HttpURLConnection http_conn = (HttpURLConnection) url_connection;
            if (http_conn.getResponseCode() != HttpURLConnection.HTTP_OK) throw new IOException(source_url.toString() + " : " + http_conn.getResponseCode() + " " + http_conn.getResponseMessage());
        }
        input_stream = url_connection.getInputStream();
        if (source_url.toString().endsWith(".gz")) input_stream = new GZIPInputStream(input_stream, 4096);
        return input_stream;
    }
