    private int sendData(UUID sessionId, String cmd, FileInputStream fis) throws SynchronizationException {
        try {
            HttpClient client = new SSLHttpClient();
            StringBuilder builder = new StringBuilder(url).append("?" + SESSION_PARAM + "=" + sessionId).append("&" + CMD_PARAM + "=" + cmd);
            Log.i(TAG, builder.toString());
            HttpPost method = httpPostMethod(builder.toString());
            String fileName = sessionId + ".cmodif";
            FileInputStreamPart part = new FileInputStreamPart("data", fileName, fis);
            MultipartEntity requestContent = new MultipartEntity(new Part[] { part });
            method.setEntity(requestContent);
            HttpResponse response = client.execute(method);
            Header header = response.getFirstHeader(HEADER_NAME);
            if (header != null && HEADER_VALUE.equals(header.getValue())) {
                int code = response.getStatusLine().getStatusCode();
                if (code == HttpStatus.SC_OK) {
                    Log.i(TAG, "Status OK");
                    InputStream is = response.getEntity().getContent();
                    StringBuffer sb = new StringBuffer();
                    int i;
                    while ((i = is.read()) != -1) {
                        sb.append((char) i);
                    }
                    Log.i(TAG, sb.toString());
                    if (sb.toString().startsWith("ACK")) {
                        String result = sb.toString().split("_")[1];
                        return result != null ? result.length() != 0 ? Integer.parseInt(result) : 0 : 0;
                    } else {
                        throw new SynchronizationException("Command 'send' : Server error code returned.");
                    }
                } else {
                    throw new Exception("HTTP error code returned : " + code);
                }
            } else {
                throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_SEND);
            }
        } catch (Exception e) {
            throw new SynchronizationException("Command 'send' -> ", e, SynchronizationException.ERROR_SEND);
        }
    }
