    public static ByteBuffer fetch(URL url, long ifModifiedSince, Map<String, String> requestParameters) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setIfModifiedSince(ifModifiedSince);
        if (requestParameters != null) {
            for (Entry<String, String> parameter : requestParameters.entrySet()) {
                connection.addRequestProperty(parameter.getKey(), parameter.getValue());
            }
        }
        int contentLength = connection.getContentLength();
        InputStream in = connection.getInputStream();
        ByteBufferOutputStream buffer = new ByteBufferOutputStream(contentLength >= 0 ? contentLength : 4 * 1024);
        try {
            buffer.transferFully(in);
        } catch (IOException e) {
            if (contentLength >= 0) {
                throw e;
            }
        } finally {
            in.close();
        }
        if (contentLength < 0 && buffer.getByteBuffer().remaining() == 0) return null;
        return buffer.getByteBuffer();
    }
