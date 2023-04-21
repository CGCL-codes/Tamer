    public int resumeRequestModification(UUID sessionId, OutputStream out, long bytesReceived) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        StringBuilder builder = new StringBuilder(url).append("?" + SESSION_PARAM + "=" + sessionId).append("&" + CMD_PARAM + "=" + CMD_RESUME_RECEIVE).append("&" + LENGTH_PARAM + "=" + String.valueOf(bytesReceived));
        HttpGet method = httpGetMethod(builder.toString());
        try {
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_NAME);
            if (header != null && HEADER_VALUE.equals(header.getValue())) {
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    long expectedLength = response.getEntity().getContentLength();
                    InputStream is = response.getEntity().getContent();
                    FileUtils.writeInFile(is, out, expectedLength);
                    return 0;
                } else {
                    throw new SynchronizationException("Resume 'request' command ->HTTP code returned. " + code, SynchronizationException.ERROR_RECEIVE);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_RECEIVE);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Resume 'request' command ->HTTP code returned.", e, SynchronizationException.ERROR_RECEIVE);
        }
    }
