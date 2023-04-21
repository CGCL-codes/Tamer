    protected ByteArrayOutputStream readFromUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty(InterceptorMapper.JMAGE_INTERNAL, TRUE);
        connection.connect();
        String responseHeader = connection.getHeaderField(null);
        assert (responseHeader != null) : HTTP_HEADER_ERROR;
        if (responseHeader.indexOf(HTTP_400) > -1) {
            throw new IOException(URL_LOADERROR + url + CAUSE + responseHeader);
        }
        InputStream is = connection.getInputStream();
        return streamConvert(is);
    }
