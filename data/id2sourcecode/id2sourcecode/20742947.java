    @Override
    public <T> T sendRequest(Request<T> msg) throws IOException {
        if (!(msg instanceof PKCSReq)) {
            throw new IllegalArgumentException("POST transport may not be used for " + msg.getOperation() + " messages.");
        }
        final URL url = getUrl(msg.getOperation());
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        final OutputStream stream = new BufferedOutputStream(conn.getOutputStream());
        try {
            msg.write(stream);
        } finally {
            stream.close();
        }
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        return msg.getContentHandler().getContent(conn.getInputStream(), conn.getContentType());
    }
