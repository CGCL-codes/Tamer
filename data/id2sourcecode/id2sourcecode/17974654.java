    public String initSession(String login, String password, String terminalId, String type) throws SynchronizationException {
        HttpClient client = new SSLHttpClient();
        if (url == null) {
            return null;
        }
        try {
            StringBuilder builder = new StringBuilder(url).append("?" + CMD_PARAM + "=" + CMD_INIT).append("&" + LOGIN_PARAM + "=" + URLEncoder.encode(login, "UTF-8")).append("&" + PASSWD_PARAM + "=" + URLEncoder.encode(password, "UTF-8")).append("&" + TERMINALID_PARAM + "=" + terminalId).append("&" + TYPE_PARAM + "=" + type);
            HttpGet get = httpGetMethod(builder.toString());
            HttpResponse response = client.execute(get);
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
                    throw new SynchronizationException("HTTP error code return : " + code, SynchronizationException.ERROR_INIT);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_INIT);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Session init -> ", e, SynchronizationException.ERROR_INIT);
        }
    }
