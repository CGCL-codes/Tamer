    private String _makeRestCall(RequestMethod requestMethod, String key, String value, HttpServletRequest httpServletRequest, String context) throws MalformedURLException, IOException, ProtocolException, UnsupportedEncodingException {
        String file = context + "/" + "rest";
        if (requestMethod.equals(RequestMethod.DELETE)) {
            file += "?key=" + key;
        }
        URL url = new URL("http", httpServletRequest.getServerName(), httpServletRequest.getServerPort(), file);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod(requestMethod.toString());
        switch(requestMethod) {
            case GET:
                {
                    break;
                }
            case PUT:
                {
                    connection.setDoOutput(true);
                    StringBuilder parameters = new StringBuilder();
                    parameters.append("key=");
                    parameters.append(URLEncoder.encode(key, "UTF-8"));
                    parameters.append("&value=");
                    parameters.append(URLEncoder.encode(value, "UTF-8"));
                    byte[] bytes = parameters.toString().getBytes();
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();
                    outputStream.close();
                    break;
                }
            case DELETE:
                {
                    break;
                }
        }
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
        int read;
        do {
            read = in.read(buffer, 0, buffer.length);
            if (read > 0) {
                out.append(buffer, 0, read);
            }
        } while (read >= 0);
        connection.getResponseCode();
        connection.disconnect();
        return out.toString();
    }
