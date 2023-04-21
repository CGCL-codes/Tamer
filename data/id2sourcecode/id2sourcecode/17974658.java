    public String resumeSend(String login, String password, String terminalId, String type, UUID sessionId) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        try {
            StringBuilder builder = new StringBuilder(url).append("?" + CMD_PARAM + "=" + CMD_RESUME_SEND_INIT).append("&" + LOGIN_PARAM + "=" + URLEncoder.encode(login, "UTF-8")).append("&" + PASSWD_PARAM + "=" + URLEncoder.encode(password, "UTF-8")).append("&" + TERMINALID_PARAM + "=" + terminalId).append("&" + TYPE_PARAM + "=" + type).append("&" + SESSION_PARAM + "=" + sessionId);
            HttpGet method = httpGetMethod(builder.toString());
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_NAME);
            if (header != null && HEADER_VALUE.equals(header.getValue())) {
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    InputStream is = response.getEntity().getContent();
                    StringBuffer sb = new StringBuffer();
                    int i;
                    while ((i = is.read()) != -1) {
                        sb.append((char) i);
                    }
                    return sb.toString();
                } else {
                    throw new SynchronizationException("HTTP error code returned :" + code, SynchronizationException.ERROR_SEND);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_SEND);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Resume 'send' session init -> ", e, SynchronizationException.ERROR_SEND);
        }
    }
