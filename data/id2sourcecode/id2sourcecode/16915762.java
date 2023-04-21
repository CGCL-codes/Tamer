    @Override
    public <T> T sendRequest(Request<T> msg) throws IOException {
        final URL url = getUrl(msg.getOperation(), msg.getMessage());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending {} to {}", msg, url);
        }
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        LOGGER.debug("Received '{} {}' when sending {} to {}", new Object[] { conn.getResponseCode(), conn.getResponseMessage(), msg, url });
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        return msg.getContentHandler().getContent(conn.getInputStream(), conn.getContentType());
    }
