    private BufferedReader getReaderFromUrl(String url) throws IOException {
        BufferedReader buffer = null;
        try {
            URL urlConn = new URL(url);
            URLConnection conn = urlConn.openConnection();
            InputStreamReader input = new InputStreamReader(conn.getInputStream(), this.charset);
            buffer = new BufferedReader(input);
            return buffer;
        } catch (IOException e) {
            throw new IOException("Error obtaining the reader from " + url);
        }
    }
