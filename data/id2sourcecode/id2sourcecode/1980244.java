    public static InputStream call(String serviceUrl, Map parameters) throws IOException, RestException {
        StringBuffer urlString = new StringBuffer(serviceUrl);
        String query = RestClient.buildQueryString(parameters);
        HttpURLConnection conn;
        if ((urlString.length() + query.length() + 1) > MAX_URI_LENGTH_FOR_GET) {
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT_STRING);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.getOutputStream().write(query.getBytes());
        } else {
            if (query.length() > 0) {
                Set parameterNames = parameters.keySet();
                StringBuffer buffer = new StringBuffer();
                for (Iterator iterator = parameterNames.iterator(); iterator.hasNext(); ) {
                    String parameterName = (String) iterator.next();
                    Object value = parameters.get(parameterName);
                    if (value instanceof String) {
                        if (parameterName.equals("query")) {
                            buffer.append(URLEncoder.encode((String) parameters.get(parameterName), "UTF-8"));
                            buffer.append("?");
                        }
                    }
                }
                urlString.append(buffer.toString()).append(query).append("&format=xml");
            }
            URL url = new URL(urlString.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT_STRING);
            conn.setRequestMethod("GET");
        }
        int responseCode = conn.getResponseCode();
        if (HttpURLConnection.HTTP_OK != responseCode) {
            ByteArrayOutputStream errorBuffer = new ByteArrayOutputStream();
            int read;
            byte[] readBuffer = new byte[ERROR_READ_BUFFER_SIZE];
            InputStream errorStream = conn.getErrorStream();
            while (-1 != (read = errorStream.read(readBuffer))) {
                errorBuffer.write(readBuffer, 0, read);
            }
            throw new RestException("Request failed, HTTP " + responseCode + ": " + conn.getResponseMessage(), errorBuffer.toByteArray());
        }
        return conn.getInputStream();
    }
