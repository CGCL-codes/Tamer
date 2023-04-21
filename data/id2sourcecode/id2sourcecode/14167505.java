    Response sendContent(String method, HttpURLConnection connection, String contentType, InputStreamReader reader) throws IOException {
        connection.setRequestMethod(method);
        connection.setRequestProperty(CONTENT_TYPE_HEADER, contentType);
        connection.setConnectTimeout(_connectTimeout);
        connection.setInstanceFollowRedirects(_followRedirects);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(connection.getOutputStream(), reader.getEncoding());
            char[] buf = new char[_bufferSize];
            for (int len = 0; (len = reader.read(buf)) != -1; ) out.write(buf, 0, len);
        } finally {
            if (out != null) out.close();
        }
        return new HttpURLConnectionWrapper(connection);
    }
