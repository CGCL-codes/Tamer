    public boolean closeSession(UUID sessionId, boolean debug) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        StringBuilder builder = new StringBuilder(url).append("?" + CMD_PARAM + "=" + CMD_CLOSE).append("&" + SESSION_PARAM + "=" + sessionId).append("&" + DEBUG_PARAM + "=" + Boolean.toString(debug));
        HttpGet method = httpGetMethod(builder.toString());
        try {
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_NAME);
            if (header != null && HEADER_VALUE.equals(header.getValue())) {
                InputStream is = response.getEntity().getContent();
                StringBuffer sb = new StringBuffer();
                int i;
                while ((i = is.read()) != -1) {
                    sb.append((char) i);
                }
                if (sb.toString().startsWith("ACK")) {
                    return true;
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_CLOSING);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Closing session -> ", e, SynchronizationException.ERROR_CLOSING);
        }
        return false;
    }
